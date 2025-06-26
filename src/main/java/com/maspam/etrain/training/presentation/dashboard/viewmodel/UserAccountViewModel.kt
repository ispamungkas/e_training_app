package com.maspam.etrain.training.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.dashboard.state.UserAccountState
import com.maspam.etrain.training.presentation.dashboard.viewmodel.DashboardViewModel.EventDashboard
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserAccountViewModel(
    private val authenticationDataSource: AuthenticationDataSource
): ViewModel() {

    private val _state = MutableStateFlow(UserAccountState())
    val state = _state
        .onStart {
            getListUser()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            UserAccountState(isLoading = true, isRefresh = false),
        )

    private val _event = Channel<EventDashboard>()
    val event = _event.receiveAsFlow()

   fun getListUser() {
       _state.update {
           it.copy(
               isLoading = true
           )
       }
       viewModelScope.launch {
           authenticationDataSource.getAllUser()
               .onError { e ->
                   _state.update {
                       it.copy(
                           isLoading = false,
                           isRefresh = false
                       )
                   }
                   _event.send(EventDashboard.Error(e))
               }
               .onSuccess { result ->
                   _state.update {
                       it.copy(
                           isLoading = false,
                           isRefresh = false,
                           data = result,
                           filteredData = result
                       )
                   }
               }
       }
   }

    fun resetPassword(nip: String) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            authenticationDataSource.changePassword(
                nip = nip,
                newPassword = nip
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false
                        )
                    }
                    _event.send(EventDashboard.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            isSuccess = true
                        )
                    }
                }
        }
    }

    fun onSearch(value: String) {
        _state.update {
            it.copy(
                filteredData = _state.value.data?.filter { data ->
                    data.name?.contains(value, true) == true
                }
            )
        }
    }


    fun dismissAlert() {
        _state.update {
            it.copy(isSuccess = false)
        }
        getListUser()
    }

    fun refresh() {
        _state.update { it.copy(isRefresh = true) }
        getListUser()
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(error = e)
        }
    }

    fun setShowModal(b: Boolean) {
        _state.update {
            it.copy(isBottomSheetShow = b)
        }
    }

    fun setSelectedNip(s: String) {
        _state.update {
            it.copy(
                selectedNip = s
            )
        }
    }
}