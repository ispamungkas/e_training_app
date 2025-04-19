package com.maspam.etrain.training.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CertificateDto(
    val id: Int?,
    val user: Int?,
    val enroll: Int?,
    val cert: String?,
)

@Serializable
data class KaryaNyataDto(
    val id: Int?,
    val att: String?,
    val enroll: Int?,
    val status: String?,
    val user: Int?
)
