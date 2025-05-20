package com.maspam.etrain.training.presentation.training.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.PostTestDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.state.PostTestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostTestViewModel (
    private val userSessionDataSource: UserSessionDataSource,
    private val postTestDataSource: PostTestDataSource,
    private val trainingDataSource: TrainingDataSource
): ViewModel() {

    private var _state = MutableStateFlow(PostTestState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun createPostTest(sectionId: Int, question: List<String>, trainId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            postTestDataSource.createPostTest(
                token = userSessionDataSource.getToken(),
                sectionId = sectionId,
                trainingId = trainId,
                question = question
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isSuccessful = true) }
                }
        }
    }

    fun getTrainingById(trainId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.getTrainingById(
                token = userSessionDataSource.getToken(),
                trainingId = trainId
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, error = error) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, data = result) }
                }
        }
    }


    fun updatePostTest(question: List<String>, postTestId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            postTestDataSource.updatePostTest(
                token = userSessionDataSource.getToken(),
                postTestId = postTestId,
                question = question
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isSuccessful = true) }
                }
        }
    }

    fun setQuestion(questionNumber: Int, value: String) {
        when(questionNumber) {
            1 -> _state.update { it.copy(question1 = value) }
            2 -> _state.update { it.copy(question2 = value) }
            3 -> _state.update { it.copy(question3 = value) }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update { it.copy(error = e) }
    }

    fun setAllReadyQuestion(question: List<String>?) {
        _state.update {
            it.copy(
                question1 = question?.get(0) ?: "",
                question2 = question?.get(1) ?: "",
                question3 = question?.get(2) ?: "",
            )
        }
    }

    fun dismissAlert() {
        _state.update { it.copy(isSuccessful = false) }
    }

    fun refresh(trainingId: Int) {
        _state.update {
            it.copy(
                isLoading = true,
                isRefresh = true,
            )
        }
        getTrainingById(trainingId)
    }


}