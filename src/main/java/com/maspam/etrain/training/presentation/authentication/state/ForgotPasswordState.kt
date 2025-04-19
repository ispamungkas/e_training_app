package com.maspam.etrain.training.presentation.authentication.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.UserModel

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val user: UserModel? = null,
    val error: NetworkError? = null
)
