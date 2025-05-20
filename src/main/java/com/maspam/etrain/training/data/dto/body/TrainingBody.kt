package com.maspam.etrain.training.data.dto.body

import android.net.Uri

data class TrainingBody(
    val name: String? = null,
    val desc: String? = null,
    val typeTrain: String = "training",
    val typeTrainAc: String = "online",
    val location: String = "-",
    val narasumber: String = "-",
    val link: String = "https://www.google.com",
    val dateline: Long? = null,
    val attend: Long? = null,
    val img: Uri? = null
)
