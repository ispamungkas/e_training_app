package com.maspam.etrain.training.presentation.taketraining.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.data.remote_datasource.local.proto.RemoteUserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.datasource.network.KaryaNyataDataSource
import com.maspam.etrain.training.domain.datasource.network.PostTestDataSource
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.taketraining.state.TakeTrainingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TakeTrainingViewModel(
    private val userSessionDataSource: RemoteUserSessionDataSource,
    private val enrollDataSource: EnrollDataSource,
    private val postTestDataSource: PostTestDataSource,
    private val karyaNyataDataSource: KaryaNyataDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(TakeTrainingState())
    val state = _state.asStateFlow()

    private val _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun setSelectedTopic(section: Int, topic: Int, isCheck: Boolean) {
        _state.update {
            it.copy(
                sectionSelection = section,
                topicSelection = topic,
                isCheck = isCheck,
                selectedTopic = if (section >= 94 || topic >= 94) TopicModel() else it.data?.trainingDetail?.sections?.get(
                    section
                )?.topics?.get(topic),
                lastTopic = _state.value.data?.sLearn ?: 0,
                lastSection = _state.value.data?.pLearn ?: 0,
            )
        }
    }

    fun setLastTopic(value: Boolean) {
        _state.update {
            it.copy(
                isLast = value
            )
        }
    }

    /**
     * Answer management
     */

    fun setAnswer(ans: String, section: Int, questionNumber: Int) {
        try {
            val current = _state.value.ansOverall.toMutableList()
            if (current.size <= section) {
                current.add(mutableListOf())
            }

            val sections = current[section].toMutableList()

            if (current[section].size <= questionNumber) {
                sections.add("")
            }

            sections[questionNumber] = ans
            current[section] = sections

            println(current)

            _state.value = _state.value.copy(
                ansOverall = current
            )
        } catch (_: IndexOutOfBoundsException) {
            _state.update {
                it.copy(
                    inputFailure = true
                )
            }
        }

    }

    /**
     * Enroll model modify
     */

    fun setInitialEnrollModel(enrollModel: EnrollModel) {
        _state.update {
            it.copy(
                data = enrollModel,
                selectedTopic = enrollModel.trainingDetail?.sections?.get(
                    0
                )?.topics?.get(0),
                lastTopic = enrollModel.sLearn ?: 0,
                lastSection = enrollModel.pLearn ?: 0,
            )
        }
    }

    fun updateProgress(enrollId: Int, section: Int, topic: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            enrollDataSource.updateAttendance(
                token = userSessionDataSource.getToken(),
                enrollId = enrollId
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result,
                            lastSection = result.pLearn ?: 0,
                            lastTopic = result.sLearn ?: 0
                        )
                    }
                }
            enrollDataSource.updateProgressTraining(
                token = userSessionDataSource.getToken(),
                enrollId = enrollId,
                section = section,
                topic = topic
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result,
                            lastSection = result.pLearn ?: 0,
                            lastTopic = result.sLearn ?: 0
                        )
                    }
                }
        }
    }

    fun takePostTest() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            enrollDataSource.takePostTest(
                token = userSessionDataSource.getToken(),
                enrollId = state.value.data?.id ?: 0
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result,
                        )
                    }
                }
        }
    }

    fun getAllPostTest() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            postTestDataSource.getPostTest(
                token = userSessionDataSource.getToken()
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            postTest = result.filter { value ->
                                value.train == _state.value.data?.train
                            }
                        )
                    }
                }
        }
    }

    fun submitPostTest() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            _state.value.ansOverall.forEachIndexed { i, data ->
                postTestDataSource.createAnswer(
                    token = userSessionDataSource.getToken(),
                    postTestId = _state.value.postTest?.get(i)?.id ?: 0,
                    userId = userSessionDataSource.getId(),
                    ans = data
                )
                    .onError { e ->
                        _globalEvent.send(GlobalEvent.Error(e))
                    }
                    .onSuccess {
                        if (i == _state.value.ansOverall.size - 1) {
                            _state.update {
                                it.copy(isSuccess = true)
                            }
                        }
                    }
            }
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun submitKaryaNyata() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            karyaNyataDataSource.uploadKaryaNyata(
                token = userSessionDataSource.getToken(),
                enrollId = _state.value.data?.id ?: 0,
                att = _state.value.file ?: Uri.parse(""),
                userId = userSessionDataSource.getId()
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            data = state.data?.copy(
                                tKaryaNyata = true,
                                karyaNyataModel = result
                            )
                        )
                    }
                }
            enrollDataSource.updateProgressTraining(
                token = userSessionDataSource.getToken(),
                enrollId = _state.value.data?.id ?: 0,
                section = 97,
                topic = 97
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            data = result,
                            lastSection = result.pLearn ?: 0,
                            lastTopic = result.sLearn ?: 0
                        )
                    }
                }
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    fun resubmitKaryaNyata() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            karyaNyataDataSource.updateAttKaryaNyata(
                token = userSessionDataSource.getToken(),
                att = _state.value.file ?: Uri.parse(""),
                karyaNyataId = _state.value.data?.karyaNyataModel?.id ?: 0
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            isSuccess = true,
                            data = state.data?.copy(
                                tKaryaNyata = true,
                                karyaNyataModel = result
                            )
                        )
                    }
                }
            enrollDataSource.updateProgressTraining(
                token = userSessionDataSource.getToken(),
                enrollId = _state.value.data?.id ?: 0,
                section = 97,
                topic = 97
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result,
                            lastSection = result.pLearn ?: 0,
                            lastTopic = result.sLearn ?: 0
                        )
                    }
                }
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    fun getCertificate() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            enrollDataSource.createCertificate(
                token = userSessionDataSource.getToken(),
                userId = userSessionDataSource.getId(),
                enrollId = _state.value.data?.id ?: 0
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            data = it.data?.copy(
                                certificate = result
                            )
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }

            enrollDataSource.updateProgressTraining(
                token = userSessionDataSource.getToken(),
                enrollId = _state.value.data?.id ?: 0,
                section = 999,
                topic = 999
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result,
                            lastSection = result.pLearn ?: 0,
                            lastTopic = result.sLearn ?: 0
                        )
                    }
                }
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    fun setFileUri(file: Uri?) {
        file?.let { value ->
            _state.update {
                it.copy(
                    file = value
                )
            }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                error = e
            )
        }
    }

    fun closeAlert() {
        _state.update {
            it.copy(
                isSuccess = false,
                inputFailure = false,
            )
        }
    }
}