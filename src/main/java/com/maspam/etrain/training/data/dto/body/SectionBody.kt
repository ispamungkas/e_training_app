package com.maspam.etrain.training.data.dto.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionBody(
    val name: String,
    val jp: Int,
    @SerialName("train_id")
    val trainId: Int
)

@Serializable
data class UpdateSectionBody(
    val name: String,
    val jp: Int,
)
