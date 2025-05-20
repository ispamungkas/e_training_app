package com.maspam.etrain.training.presentation.training.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.state.DetailTrainingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailTrainingViewModel(
    private val trainingDataSource: TrainingDataSource,
    private val userSessionDataSource: UserSessionDataSource,
): ViewModel() {

    private var _state = MutableStateFlow(DetailTrainingState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getTrainingById() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.getTrainingById(
                token = userSessionDataSource.getToken(),
                trainingId = _state.value.data?.id ?: 0
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

    fun setPublishedTraining(isPublish: Boolean) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.setPublishedTraining(
                token = userSessionDataSource.getToken(),
                isPublish = isPublish,
                trainingId = _state.value.data?.id ?: 0
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

    fun setInitialValue(trainingModel: TrainingModel) {
        _state.update {
            it.copy(
                data = trainingModel,
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
}