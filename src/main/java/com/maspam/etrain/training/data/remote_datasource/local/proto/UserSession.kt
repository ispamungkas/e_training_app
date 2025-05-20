package com.maspam.etrain.training.data.remote_datasource.local.proto

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val token: String? = "",
    val id: Int? = null,
    val nip: String? = "",
    val role: String? = "",
    val name: String? = "",
    val image: String? = "",
    val enroll: List<Int> = emptyList()
)
