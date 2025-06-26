package com.maspam.etrain.training.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class NewsModel(
    val id: Int? = 0,
    @SerialName("img")
    val image: String? = "",
    val name: String? = "",
    val desc: String? = "",
    val author: String? = "",
    @SerialName("publish_date")
    val publishDate: Long? = 0,
)