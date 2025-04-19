package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class KaryaNyataModel (
    val id : Int? = 9,
    val att: String? = "",
    val status: String? = "",
)