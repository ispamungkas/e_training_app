package com.maspam.etrain.training.presentation.profile.event

sealed class ChangePasswordProfileEvent {
    data class PasswordForm(val password: String): ChangePasswordProfileEvent()
    data class ConfirmPasswordForm(val confirmPassword: String): ChangePasswordProfileEvent()

    object submit: ChangePasswordProfileEvent()
}