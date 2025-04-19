package com.maspam.etrain.training.presentation.profile.state

import com.maspam.etrain.training.core.domain.utils.NetworkError

data class ChangePasswordProfileState(
    val isLoading: Boolean = false,
    val error: NetworkError? = null,

    val password: String? = "",
    val passwordError: String? = "",
    val confirmPassword: String? = "",
    val confirmPasswordError: String? = ""
)
