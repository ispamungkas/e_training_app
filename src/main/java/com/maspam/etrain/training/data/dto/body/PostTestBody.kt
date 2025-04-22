package com.maspam.etrain.training.data.dto.body

import kotlinx.serialization.Serializable

@Serializable
data class PostTestBody(
    val train: Int,
    val section: Int,
    val question: List<String>
)
