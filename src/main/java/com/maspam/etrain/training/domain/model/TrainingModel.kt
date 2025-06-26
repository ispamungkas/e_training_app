package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrainingModel(
    val id: Int? = null,
    val image: String? = "",
    val name: String? = "",
    val typeTraining: String? = "",
    val typeTrainingCategory: String? = "",
    val isOpen: Boolean? = false,
    val isPublish: Boolean? = false,
    val link: String? = "",
    val location: String? = "",
    val desc: String? = "",
    val totalTaken: Int? = 0,
    val due: Long? = 0L,
    val attend: Long? = 0L,
    val totalJp: Int? = 0,
    val postTest: List<PostTestModel>? = emptyList(),
    val sections: List<SectionModel>? = emptyList(),
    val createdAt: Long? = 0L
)
