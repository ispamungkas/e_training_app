package com.maspam.etrain.training.data.dto.body

import kotlinx.serialization.Serializable

@Serializable
data class AnswerBody(
    val post: Int,
    val user: Int,
    val ans: List<String>
)
