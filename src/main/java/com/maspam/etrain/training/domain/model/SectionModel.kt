package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SectionModel(
    val id: Int? = 0,
    val name: String? = "",
    val jp: Int? = 0,
    val status: String? = "",
    val trainId: Int? = 0,
    val topics: List<TopicModel>? = null
)
