package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicModel(
    val id: Int? = 0,
    val name: String? = "",
    val content: String? = "",
    val topicImage: String? = ""
)
