package com.maspam.etrain.training.presentation.authentication.state

import com.maspam.etrain.training.core.domain.utils.NetworkError

data class ChangePasswordState(
    val nip: String? = "",
    val nipError: String? = "",
    val newPassword: String? = "",
    val newPasswordError: String? = "",
    val confirmNewPassword: String? = "",
    val confirmNewPasswordError: String? = "",

    val error: NetworkError? = null,
    val isSuccess: Boolean? = false,
    val isLoading: Boolean? = false,
)
