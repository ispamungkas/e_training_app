package com.maspam.etrain.training.data.dto.body

import android.net.Uri

data class TopicBody(
    val topicId: Int? = 0,
    val name: String? = "",
    val content: String? = "",
    val linkVideo: String? = "",
    val img: Uri? = Uri.parse("")
)
