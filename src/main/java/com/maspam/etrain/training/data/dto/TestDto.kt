package com.maspam.etrain.training.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostTestDto(
    val id: Int? = 0,
    val train: Int? = 0,
    val question: List<String>? = null,
    val answers: List< AnswerDto>? = null,
    val section: Int? = 0
)

@Serializable
data class AnswerDto(
    val id: Int? = 0,
    val post: Int? = 0,
    val user: Int? = 0,
    val ans: List<String>? = null
)
