package com.maspam.etrain.training.presentation.training.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.data.dto.body.TopicBody
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.event.FormTopicTrainingEvent
import com.maspam.etrain.training.presentation.training.state.ListTopicState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListTopicViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(ListTopicState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun onChangeAction(action: FormTopicTrainingEvent) {
        when (action) {
            is FormTopicTrainingEvent.NameChange -> _state.update { data ->
                data.copy(
                    selectedData = data.selectedData?.copy(
                        name = action.name
                    )
                )
            }
            is FormTopicTrainingEvent.ContentChange -> _state.update { data ->
                data.copy(
                    selectedData = data.selectedData?.copy(
                        content = action.content
                    )
                )
            }
            is FormTopicTrainingEvent.ImgChange -> _state.update { data ->
                data.copy(
                    selectedData = data.selectedData?.copy(
                        img = action.img
                    )
                )
            }
            is FormTopicTrainingEvent.Submit -> {
                updateTopic(action.topicBody)
            }

            is FormTopicTrainingEvent.LinkVideoChange -> _state.update { data ->
                data.copy(
                    selectedData = data.selectedData?.copy(
                        linkVideo = action.link
                    )
                )
            }
        }
    }

    fun getAllTopic() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.getAllTopic(
                token = userSessionDataSource.getToken(),
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update {
                    it.copy(isLoading = false,
                        isRefresh = false,
                        data = result.filter { it.sectionId == _state.value.sectionId }
                            .sortedByDescending { it.id })
                }
            }
        }
    }

    fun deleteSection(topicId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.deleteTopicById(
                token = userSessionDataSource.getToken(), topicId = topicId
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
            }

            trainingDataSource.getAllTopic(
                token = userSessionDataSource.getToken(),
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update {
                    it.copy(isLoading = false,
                        isRefresh = false,
                        data = result.filter { it.sectionId == _state.value.sectionId }
                            .sortedByDescending { it.id },
                        isSuccess = true
                    )
                }
            }
        }
    }

    fun setTopic(topicName: String, img: Uri, content: String, sectionId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.addTopic(
                token = userSessionDataSource.getToken(),
                name = topicName,
                sectionId = sectionId,
                content = content,
                image = img,
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
            }

            trainingDataSource.getAllTopic(
                token = userSessionDataSource.getToken(),
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update {
                    it.copy(isLoading = false,
                        isRefresh = false,
                        data = result.filter { it.sectionId == _state.value.sectionId }
                            .sortedByDescending { it.id },
                        isSuccess = true
                    )
                }
            }
        }
    }

    fun updateTopic(topicBody: TopicBody) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            trainingDataSource.updateTopic(
                token = userSessionDataSource.getToken(),
                topicBody = topicBody
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update { it.copy(isLoading = false, isRefresh = false, isSuccess = true) }
            }

            trainingDataSource.getAllTopic(
                token = userSessionDataSource.getToken(),
            ).onError { error ->
                _state.update { it.copy(isLoading = false, isRefresh = false) }
                _globalEvent.send(GlobalEvent.Error(e = error))
            }.onSuccess { result ->
                _state.update {
                    it.copy(isLoading = false,
                        isRefresh = false,
                        data = result.filter { it.sectionId == _state.value.sectionId }
                            .sortedByDescending { it.id },
                        isSuccess = true
                    )
                }
            }
        }
    }

    fun setShowConfirmation(value: Boolean) {
        _state.update {
            it.copy(
                showConfirmation = value
            )
        }
    }

    fun selectedTopicViewModel(value: TopicModel) {
        _state.update {
            it.copy(
                isPictureWasAdded = if (value.topicImage?.isNotBlank() == true) true else false,
                selectedData = TopicBody(
                    name = value.name,
                    topicId = value.id,
                    content = value.content,
                )
            )
        }
    }

    fun setTopicId(value: Int) {
        _state.update {
            it.copy(
                topicId = value
            )
        }
    }

    fun setInitialValue(topicList: List<TopicModel>, sectionId: Int) {
        _state.update {
            it.copy(
                data = topicList, sectionId = sectionId
            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(error = e)
        }
    }

    fun refresh() {
        _state.update { it.copy(isRefresh = true) }
    }

    fun dismissAlert() {
        _state.update { it.copy(isSuccess = false) }
    }

    fun setShowModal(value: Boolean) {
        _state.update { it.copy(showModal = value) }
    }

}