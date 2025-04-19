package com.maspam.etrain.training.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    val id: Int?,
    val name: String?,
    val status: String?,
    val jp: Int?,

    @SerialName("train_id")
    val trainId: Int?,

    @SerialName("created_at")
    val createdAt: Int?,

    @SerialName("deleted_at")
    val deletedAt: Int?,

    @SerialName("updated_at")
    val updatedAt: Int?,

    val topics: List<TopicDto>?,

)
