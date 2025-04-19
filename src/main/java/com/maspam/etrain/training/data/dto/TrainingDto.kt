package com.maspam.etrain.training.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainingDto(
    val id: Int? = 0,
    val name: String? = "",
    val desc: String? = "",

    @SerialName("type_train")
    val typeTrain: String? = "",

    @SerialName("type_train_ac")
    val typeTrainAc: String? = "",

    val location: String? = "",
    val link: String? = "",
    val dateline: Long? = 0,

    @SerialName("total_jp")
    val totalJp: Int? = 0,

    @SerialName("is_open")
    val isOpen: Boolean? = false,

    @SerialName("is_publish")
    val isPublish: Boolean? = false,

    val img: String? = "",
    val attend: Long? = 0,

    @SerialName("created_at")
    val createdAt: Long? = 0,

    @SerialName("deleted_at")
    val deletedAt: Int? = 0,

    @SerialName("updated_at")
    val updatedAt: Int? = 0,

    val sections: List<SectionDto>? = emptyList(),

    @SerialName("post_tests")
    val postTest: List<PostTestDto>? = emptyList(),

    val enrolls: List<Int>? = emptyList()
)
