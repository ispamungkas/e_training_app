package com.maspam.etrain.training.presentation.training.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.training.state.TrainingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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

    fun getListTraining() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.getAllTraining(
                token = userSessionDataSource.getToken()
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, listTraining = result) }
                }
        }
    }
}