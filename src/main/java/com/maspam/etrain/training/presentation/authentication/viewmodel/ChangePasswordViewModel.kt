package com.maspam.etrain.training.presentation.authentication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.R
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.core.presentation.utils.formValidation.CommonValidation
import com.maspam.etrain.training.core.presentation.utils.formValidation.RepeatedPasswordValidation
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.authentication.event.ChangePasswordFormEvent
import com.maspam.etrain.training.presentation.authentication.state.ChangePasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authenticationDataSource: AuthenticationDataSource,
    context: Context,
): ViewModel() {

    private val nipValidation: CommonValidation = CommonValidation(context = context, fieldNameValidation = "Nip")
    private val passwordValidation: CommonValidation = CommonValidation(context = context, fieldNameValidation = context.getString(
        R.string.password))
    private val confirmPasswordValidation: RepeatedPasswordValidation = RepeatedPasswordValidation(context = context)

    private var _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChangePasswordFormEvent) {
        when(event) {
            is ChangePasswordFormEvent.ConfirmPasswordForm -> {
                _state.update { it.copy(confirmNewPassword = event.confirmPassword) }
            }
            is ChangePasswordFormEvent.PasswordForm -> {
                _state.update { it.copy(newPassword = event.password) }
            }
            is ChangePasswordFormEvent.NipForm -> {
                _state.update { it.copy(nip = event.nip) }
            }
            is ChangePasswordFormEvent.submit -> {
                submit()
            }
        }
    }

    fun submit() {
        val nip = nipValidation.execute(state.value.nip ?: "")
        val passwordResult = passwordValidation.execute(state.value.newPassword ?: "")
        val confirmPasswordResult = confirmPasswordValidation.execute(state.value.newPassword ?: "",state.value.confirmNewPassword ?: "")

        val hasError = listOf(
            nip, passwordResult, confirmPasswordResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                nipError = nip.errorMessage,
                newPasswordError = passwordResult.errorMessage,
                confirmNewPasswordError = confirmPasswordResult.errorMessage
            )

            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            authenticationDataSource.changePassword(
                nip = _state.value.nip ?: "",
                newPassword = _state.value.newPassword ?: ""
            )
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = true,
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        error = error,
                        isSuccess = false,
                    ) }
                }
        }
    }

    fun setError(error: NetworkError?) {
        _state.update { it.copy(error = null) }
    }

    fun setNip(nip: String) {
        _state.update { it.copy(nip =  nip) }
    }

}