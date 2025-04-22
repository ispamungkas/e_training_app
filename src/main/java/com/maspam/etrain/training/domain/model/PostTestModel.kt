package com.maspam.etrain.training.domain.model

import com.maspam.etrain.training.data.dto.AnswerDto
import kotlinx.serialization.Serializable

@Serializable
data class PostTestModel(
    val id: Int? = 0,
    val train: Int? = 0,
    val question: List<String>? = null,
    val answers: List<AnswerDto>? = null,
    val section: Int? = 0
)