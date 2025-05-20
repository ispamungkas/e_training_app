package com.maspam.etrain.training.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val enrollDataSource: EnrollDataSource,
    private val userSessionDataSource: UserSessionDataSource
): ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    private val _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun check(value: String) {
        _state.update {
            it.copy(
                isFirst = false,
                isLoading = true
            )
        }
        viewModelScope.launch {
            enrollDataSource.checkCertificate(
                token = userSessionDataSource.getToken(),
                value = value
            )
                .onError { e ->
                    if (e.name == NetworkError.NOT_ACCEPTED.name) {
                        _state.update {
                            it.copy(
                                isFailed = true,
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        _globalEvent.send(GlobalEvent.Error(e))
                    }
                }
                .onSuccess {
                    _state.update {
                        it.copy(
                            isSuccess = true,
                            isLoading = false,
                            isVerified = true,
                            isFirst = true,
                        )
                    }
                }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                isError = e
            )
        }
    }

    fun clear() {
        _state.update {
            it.copy(
                isLoading = false,
                isFirst = true,
                isSuccess = null,
                isError = null,
                isFailed = null,
                isVerified = null
            )
        }
    }

}