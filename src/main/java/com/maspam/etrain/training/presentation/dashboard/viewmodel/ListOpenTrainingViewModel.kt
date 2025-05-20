package com.maspam.etrain.training.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.dashboard.state.ListOpenTrainingState
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListOpenTrainingViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource,
    private val enrollDataSource: EnrollDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(ListOpenTrainingState())
    val state = _state
        .onStart {
            getOpenTraining()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ListOpenTrainingState()
        )

    private val _event = Channel<GlobalEvent>()
    val event = _event.receiveAsFlow()

    fun getOpenTraining() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.getOpenTraining(
                token = userSessionDataSource.getToken(),
            )
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _event.send(GlobalEvent.Error(error))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result.sortedByDescending { train -> train.id }.filter { data -> !userSessionDataSource.getUserSession().enroll.contains(data.id) && data.isOpen == true && data.isPublish == true}
                        )
                    }
                }
        }
    }

    fun enrollTraining(trainingId: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            enrollDataSource.enrollTraining(
                token = userSessionDataSource.getToken(),
                userId = userSessionDataSource.getId(),
                trainingId = trainingId
            )
                .onError { e ->
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                    _event.send(GlobalEvent.Error(e))
                }
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update { it.copy(error = e) }
    }

    fun setModalBottomSheetState(value: Boolean, id: Int? = null) {
        _state.update {
            it.copy(
                isBottomSheetShow = value,
                selectedTrain = id?.let { selectedId ->
                    it.data?.first { data -> data.id == selectedId }
                }
            )
        }
    }

}