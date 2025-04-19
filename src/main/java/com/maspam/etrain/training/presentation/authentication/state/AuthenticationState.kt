package com.maspam.etrain.training.presentation.authentication.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.UserModel

data class AuthenticationState(
    val nip: String? = "",
    val password: String? = "",
    val name: String? = "",
    val isHeadCheck: Boolean? = false,

    val nipErrorMessage: String? = "",
    val passwordErrorMessage: String? = "",
    val nameErrorMessage: String? = "",

    val user: UserModel? = null,
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false,
    val errorResult: NetworkError? = null
)
