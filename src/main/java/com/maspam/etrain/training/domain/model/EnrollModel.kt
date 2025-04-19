package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EnrollModel(
    val id: Int? = 0,
    val train: Int? = 0,
    val status: String? = "",
    val outDate: Long? = 0L,
    val sLearn: Int? = 0,
    val pLearn: Int? = 0,
    val certificate: CertificateModel? = null,
    val karyaNyataModel: KaryaNyataModel? = null,
    val totalJp: Int? = 0,
    val trainingDetail: TrainingModel? = null,
)
