package com.maspam.etrain.training.presentation.training.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.state.TrainingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(TrainingState())
    val state = _state
        .onStart {
            getListTraining()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            TrainingState()
        )

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getListTraining() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.getAllTraining(
                token = userSessionDataSource.getToken()
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, error = error) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, listTraining = result) }
                }
        }
    }

    fun setValueOfSearch(value: String) {
        _state.update {
            it.copy(
                search = value
            )
        }
    }

    fun setFilter(value: Boolean?) {
        _state.update { state ->
            state.copy(
                filterByStatus = value,
                filteredList = value?.let {
                    state.filteredList?.filter { data ->
                        data.isOpen == value
                    }
                } ?: state.filteredList
            )
        }
    }

    fun setFilterPublish(value: Boolean) {
        _state.update { state ->
            state.copy(
                filterByStatus = value,
                filteredList = value.let {
                    state.filteredList?.filter { data ->
                        data.isPublish == value
                    }
                } ?: state.filteredList
            )
        }
    }

    fun refresh() {
        _state.update {
            it.copy(
                isRefresh = true
            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(error = e)
        }
    }
}