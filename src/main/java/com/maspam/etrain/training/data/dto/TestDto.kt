package com.maspam.etrain.training.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostTestDto(
    val id: Int?,
    val train: Int?,
    val question: List<String>?,
    val answers: List< AnswerDto>?,
    val section: Int?
)

@Serializable
data class AnswerDto(
    val id: Int?,
    val post: Int?,
    val user: Int?,
    val ans: List<String>?
)
