package com.maspam.etrain.training.presentation.authentication.event

sealed class ChangePasswordFormEvent {
    data class PasswordForm(val password: String): ChangePasswordFormEvent()
    data class ConfirmPasswordForm(val confirmPassword: String): ChangePasswordFormEvent()
    data class NipForm(val nip: String): ChangePasswordFormEvent()

    object submit: ChangePasswordFormEvent()
}