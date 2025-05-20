package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class KaryaNyataModel (
    val id : Int? = 0,
    val att: String? = "",
    val status: String? = "",
    val enroll: Int? = 0,
    val user: Int? = 0
)