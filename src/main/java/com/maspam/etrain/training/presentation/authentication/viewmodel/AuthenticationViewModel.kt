package com.maspam.etrain.training.presentation.authentication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.R
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.core.presentation.utils.formValidation.CommonValidation
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.authentication.event.AuthenticationFormEvent
import com.maspam.etrain.training.presentation.authentication.state.AuthenticationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authenticationDataSource: AuthenticationDataSource,
    private val userSessionDataSource: UserSessionDataSource,
    context: Context,
) : ViewModel() {

    private val nipValidation = CommonValidation(context = context, "Nip")
    private val passwordValidation = CommonValidation(
        context = context,
        context.getString(R.string.password)
    )
    private val nameValidation = CommonValidation(
        context = context,
        context.getString(R.string.name)
    )

    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    private val _initialState = MutableStateFlow(AuthenticationState())
    val initialState = _initialState.asStateFlow()

    private val _event = Channel<AuthenticationEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event: AuthenticationFormEvent) {
        when (event) {
            is AuthenticationFormEvent.NameChanged -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is AuthenticationFormEvent.NipChanged -> {
                _state.update {
                    it.copy(
                        nip = event.nip
                    )
                }
            }

            is AuthenticationFormEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }

            is AuthenticationFormEvent.RoleChanged -> {
                _state.update {
                    it.copy(
                        role = event.role
                    )
                }
            }

            is AuthenticationFormEvent.submitLogin -> {
                submitLogin()
            }

            is AuthenticationFormEvent.submitRegister -> {
                submitRegister()
            }
        }
    }

    private fun submitLogin() {
        val nipResult = nipValidation.execute(_state.value.nip ?: "")
        val passwordResult = passwordValidation.execute(_state.value.password ?: "")

        val hasError = listOf(
            nipResult, passwordResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    nipErrorMessage = nipResult.errorMessage,
                    passwordErrorMessage = passwordResult.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authenticationDataSource.login(
                nip = _state.value.nip ?: "",
                password = _state.value.password ?: "",
            )
                .onSuccess { res ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = res
                        )
                    }

                    userSessionDataSource.update(data = res)

                    _event.send(AuthenticationEvent.Success)
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _event.send(
                        AuthenticationEvent.Error(e = error)
                    )
                }
        }
    }

    fun submitRegister() {
        val nipResult = nipValidation.execute(_state.value.nip ?: "")
        val nameResult = nameValidation.execute(_state.value.name ?: "")

        val hasError = listOf(
            nipResult, nameResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    nipErrorMessage = nipResult.errorMessage,
                    nameErrorMessage = nameResult.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authenticationDataSource.register(
                nip = _state.value.nip ?: "",
                name = _state.value.name ?: "",
                role = _state.value.role ?: "Teacher"
            )
                .onSuccess { res ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = res
                        )
                    }
                    _event.send(AuthenticationEvent.Success)
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _event.send(
                        AuthenticationEvent.Error(e = error)
                    )
                }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                errorResult = e
            )
        }
    }

    fun setSuccess(value: Boolean) {
        _state.update {
            it.copy(
                isSuccess = value
            )
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val data = userSessionDataSource.getUserSession()
            _initialState.update {
                it.copy(userSession = data)
            }
        }
    }

    sealed interface AuthenticationEvent {
        data class Error(val e: NetworkError) : AuthenticationEvent
        object Success : AuthenticationEvent
    }

}