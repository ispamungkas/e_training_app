package com.maspam.etrain.training.presentation.profile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.R
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.core.presentation.utils.formValidation.CommonValidation
import com.maspam.etrain.training.core.presentation.utils.formValidation.RepeatedPasswordValidation
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.profile.event.ChangePasswordProfileEvent
import com.maspam.etrain.training.presentation.profile.state.ChangePasswordProfileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdatePasswordProfileViewModel(
    private val authenticationDataSource: AuthenticationDataSource,
    private val userSessionDataSource: UserSessionDataSource,
    context: Context
): ViewModel() {

    private val passwordValidation: CommonValidation = CommonValidation(context = context, fieldNameValidation = context.getString(
        R.string.password))
    private val confirmPasswordValidation: RepeatedPasswordValidation = RepeatedPasswordValidation(context = context)

    private var _state = MutableStateFlow(ChangePasswordProfileState())
    val state = _state.asStateFlow()

    private var _event = Channel<OnChangePasswordProfileEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: ChangePasswordProfileEvent) {
        when (action) {
            is ChangePasswordProfileEvent.ConfirmPasswordForm -> { _state.update { it.copy(confirmPassword = action.confirmPassword) }}
            is ChangePasswordProfileEvent.PasswordForm -> { _state.update { it.copy(password = action.password) } }
            ChangePasswordProfileEvent.submit -> changePassword()
        }
    }

    fun changePassword() {
        val passwordResult = passwordValidation.execute(state.value.password ?: "")
        val confirmPasswordResult = confirmPasswordValidation.execute(state.value.password ?: "",state.value.confirmPassword ?: "")

        val hasError = listOf(
            passwordResult, confirmPasswordResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage
            )
            return
        }

        _state.update { it.copy(
            isLoading = true
        ) }

        viewModelScope.launch {
            authenticationDataSource.changePassword(
                nip = userSessionDataSource.getNip(),
                newPassword = state.value.password ?: ""
            )
                .onSuccess { result ->
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                    _event.send(
                        OnChangePasswordProfileEvent.Success
                    )
                }
                .onError { error ->
                    _state.update { it.copy(
                        isLoading = false,
                    ) }
                    _event.send(
                        OnChangePasswordProfileEvent.Failure(e = error)
                    )
                }
        }
    }

    fun setError(e: NetworkError?)  {
        _state.update { it.copy(
            error = e
        ) }
    }

    sealed class OnChangePasswordProfileEvent {
        object Success: OnChangePasswordProfileEvent()
        data class Failure(val e: NetworkError?): OnChangePasswordProfileEvent()
    }
}