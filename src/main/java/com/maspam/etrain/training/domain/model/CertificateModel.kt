package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CertificateModel(
    val id: Int? = 0,
    val cert: String? = ""
)
