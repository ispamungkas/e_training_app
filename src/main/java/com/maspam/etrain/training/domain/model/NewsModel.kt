package com.maspam.etrain.training.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class NewsModel(
    val image: String? = "",
    val name: String? = "",
    val desc: String? = "",
    val author: String? = "",
    val publishDate: Long? = 0,
)