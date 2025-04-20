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
    val desc: String? = "",
    val totalTaken: Int? = 0,
    val due: Long? = 0L,
    val totalJp: Int? = 0,
    val postTest: List<PostTestModel>? = emptyList(),
    val sections: List<SectionModel>? = emptyList()
)
