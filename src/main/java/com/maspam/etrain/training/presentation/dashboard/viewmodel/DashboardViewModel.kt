package com.maspam.etrain.training.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.presentation.dashboard.state.DashboardState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource,
    private val enrollDataSource: EnrollDataSource,
    private val newsDataSource: NewsDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state
        .onStart {
            getNews()
            getOpenTraining()
            getUser()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            DashboardState(isLoading = true, isRefresh = false),
        )

    private val _event = Channel<EventDashboard>()
    val event = _event.receiveAsFlow()

    fun getUser() {
        viewModelScope.launch {
           _state.update { it.copy(user = userSessionDataSource.getUserSession()) }

        }
    }

    fun getNews() {
        viewModelScope.launch {
            newsDataSource.getAllNews()
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            error = error
                        )
                    }
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            news = result.slice(0..3)
                        )
                    }
                }
        }
    }

    fun getOpenTraining() {
        viewModelScope.launch {
            trainingDataSource.getOpenTraining(
                token = userSessionDataSource.getToken(),
            )
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            error = error
                        )
                    }
                    _event.send(EventDashboard.Error(error))
                }
                .onSuccess { result ->
                    val tTraining = userSessionDataSource.getTakeTraining()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            openTrain = result.sortedByDescending { train -> train.id }.filter { data ->  data.isOpen == true && data.isPublish == true && !tTraining.contains(data.id ?: 0) }
                        )
                    }
                    println(tTraining)
                    println(_state.value.openTrain?.map { it.id })
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
                        isLoading = false,
                        error = e
                    ) }
                    _event.send(EventDashboard.Error(e))
                }
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccessful = true
                    ) }
                    userSessionDataSource.updateTakenTraining(trainingId)
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

    fun setModalBottomSheetState(value: Boolean, id: Int? = null) {
        _state.update {
            it.copy(
                isBottomSheetShow = value,
                selectedTrain = id?.let { selectedId ->
                    it.openTrain?.first { openTrain -> openTrain.id == selectedId }
                }
            )
        }
    }

    fun refresh() {
        _state.update {
            it.copy(
                isRefresh = true,
                isLoading = true
            )
        }
    }

    fun dismisDialog() {
        _state.update { it.copy(isSuccessful = false) }
    }


    sealed class EventDashboard {
        data class Error(val e: NetworkError) : EventDashboard()
    }

}