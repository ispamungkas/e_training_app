package com.maspam.etrain.training.presentation.authentication.event

sealed class AuthenticationFormEvent {
    data class NipChanged(val nip: String): AuthenticationFormEvent()
    data class PasswordChanged(val password: String): AuthenticationFormEvent()
    data class NameChanged(val name: String): AuthenticationFormEvent()
    data class RoleChanged(val role: String): AuthenticationFormEvent()

    object submitLogin: AuthenticationFormEvent()
    object submitRegister: AuthenticationFormEvent()
}