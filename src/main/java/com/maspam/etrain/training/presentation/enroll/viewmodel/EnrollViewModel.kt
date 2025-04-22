package com.maspam.etrain.training.presentation.enroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.presentation.enroll.state.EnrollState
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnrollViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val enrollDataSource: EnrollDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(EnrollState())
    val state = _state.asStateFlow()

    private val _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun setAbsence(enrollId: Int) {
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
                            isSuccess = true,
                            data = result,
                        )
                    }
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

    fun getCertificate(enrollId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            enrollDataSource.createCertificate(
                token = userSessionDataSource.getToken(),
                userId = userSessionDataSource.getId(),
                enrollId = enrollId
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            certificate = result,
                            downloadSuccess = true
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
        }
    }

    fun dismissAlert() {
        _state.update {
            it.copy(
                isSuccess = false,
                downloadSuccess = false
            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                error = e
            )
        }
    }

}