package com.maspam.etrain.training.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    val id: Int?,

    @SerialName("section_id")
    val sectionId: Int?,

    val name: String?,
    val content: String?,
    val img: String?,

    @SerialName("link_video")
    val linkVideo: String?,

    @SerialName("created_at")
    val createdAt: Int?,

    @SerialName("deleted_at")
    val deletedAt: Int?,

    @SerialName("updated_at")
    val updatedAt: Int?
)
