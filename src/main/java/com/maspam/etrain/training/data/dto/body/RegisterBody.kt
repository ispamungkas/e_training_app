package com.maspam.etrain.training.data.dto.body

import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val nip: String,
    val name: String,
    val ishead: Boolean
)
