package com.maspam.etrain.training.data.dto.body

import android.net.Uri

data class NewsBody(
    val id: Int? = 0,
    val image: Uri? = Uri.parse(""),
    val name: String? = "",
    val desc: String? = "",
    val author: String? = "",
    val publishDate: Long? = null,
)
