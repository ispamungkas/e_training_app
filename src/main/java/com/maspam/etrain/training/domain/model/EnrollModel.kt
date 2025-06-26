package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EnrollModel(
    val id: Int? = 0,
    val train: Int? = 0,
    val status: String? = "",
    val outDate: Long? = 0L,
    val attandance: Boolean? = false,
    val sLearn: Int? = 0,
    val pLearn: Int? = 0,
    val tPost : Boolean? = false,
    val certificate: CertificateModel? = null,
    val karyaNyataModel: KaryaNyataModel? = null,
    val totalJp: Int? = 0,
    val tKaryaNyata: Boolean? = karyaNyataModel?.let {
        it.status == "accepted" || it.status == "pending"
    } ?: false,

    val trainingDetail: TrainingModel? = null,
    val grade: String? = null,
)
