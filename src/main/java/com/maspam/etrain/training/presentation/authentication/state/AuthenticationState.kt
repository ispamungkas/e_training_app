package com.maspam.etrain.training.presentation.authentication.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.data.remote_datasource.local.proto.UserSession
import com.maspam.etrain.training.domain.model.UserModel

data class AuthenticationState(
    val nip: String? = "",
    val password: String? = "",
    val name: String? = "",
    val role: String? = "",

    val options: List<String> = listOf("Kapala Bidang", "Teacher", "Head School"),
    var expanded: Boolean = false,

    val nipErrorMessage: String? = "",
    val passwordErrorMessage: String? = "",
    val nameErrorMessage: String? = "",

    val user: UserModel? = null,
    val userSession: UserSession? = null,
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false,
    val errorResult: NetworkError? = null
)
