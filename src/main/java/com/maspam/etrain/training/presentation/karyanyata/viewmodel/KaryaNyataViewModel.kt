package com.maspam.etrain.training.presentation.karyanyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.datasource.network.KaryaNyataDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.karyanyata.state.KaryaNyataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KaryaNyataViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val enrollDataSource: EnrollDataSource,
    private val karyaNyataDataSource: KaryaNyataDataSource,
    private val userDataSource: AuthenticationDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(KaryaNyataState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun setInitialKaryaNyata(trainingId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            enrollDataSource.getAllEnroll(
                token = userSessionDataSource.getToken(),
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            enroll = result.sortedByDescending { enroll ->
                                enroll.id
                            }.filter { data ->
                                data.train == trainingId && data.karyaNyataModel != null && data.tKaryaNyata == true
                            },
                        )
                    }
                }
        }
    }

    fun getKaryaNyataById(karyaNyataId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            karyaNyataDataSource.getKaryaNyataById(
                token = userSessionDataSource.getToken(),
                karyaNyataId = karyaNyataId
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    /**
                     * Get user for karya nyata detail
                     */
                    userDataSource.getUser(
                        id = result.user ?: 0
                    ).onSuccess { userResult ->
                        _state.update {
                            it.copy(
                                karyaNyata = result,
                                isLoading = false,
                                isRefresh = false,
                                user = userResult
                            )
                        }
                    }.onError { e2 ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefresh = false,
                            )
                        }
                        _globalEvent.send(GlobalEvent.Error(e2))
                    }

                }
        }
    }

    fun updateKaryaNyata(status: String, grade: String?) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            karyaNyataDataSource.updateStatusUploadKaryaNyata(
                token = userSessionDataSource.getToken(),
                karyaNyataId = _state.value.karyaNyata?.id ?: 0,
                enrollId = _state.value.karyaNyata?.enroll ?: 0,
                status = status,
                grade = grade
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->

                    _state.update {
                        it.copy(
                            karyaNyata = result,
                            isLoading = false,
                            isRefresh = false,
                            isSuccess = true
                        )
                    }

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

    fun setGrade(value: String) {
        _state.update {
            it.copy(
                grade = value
            )
        }
    }

}