package com.maspam.etrain.training.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BaseDto<T>(
    val message: String? = null,
    val data: T? = null
)

@Serializable
data class BaseDtoUser<T>(
    val token: String? = null,
    val message: String? = null,
    val data: T? = null
)
