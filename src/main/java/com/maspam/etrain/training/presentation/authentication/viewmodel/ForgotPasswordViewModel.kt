package com.maspam.etrain.training.presentation.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.authentication.state.ForgotPasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authenticationDataSource: AuthenticationDataSource,
): ViewModel() {

    private var _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    fun requestOtp(nip: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            authenticationDataSource.generateOtp(nip = nip)
                .onSuccess { result ->
                    result.let { res ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                }
                .onError { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        isError = true,
                        error = error
                    ) }
                }
        }
    }

    fun onDismissError() {
        _state.update {it.copy(
            isError = false,
            error = null
        )}
    }

    fun setSuccess(value: Boolean) {
        _state.update { it.copy(
            isSuccess = value
        ) }
    }

}