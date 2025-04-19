package com.maspam.etrain.training.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnrollDto(
    val id: Int?,
    val train: Int?,
    val user: Int? = 0,
    val status: String?,

    @SerialName("out_date")
    val outDate: Long?,

    @SerialName("p_learn")
    val pLearn: Int? = 0,

    @SerialName("s_learn")
    val sLearn: Int? = 0,
    val attendance: Boolean? = false,
    val certificate: List<CertificateDto>? = null,
    val karyanyata: List<KaryaNyataDto>? = null,

    @SerialName("t_jp")
    val totalJp: Int?,

    @SerialName("training_detail")
    val trainingDetail: TrainingDto? = TrainingDto(),
)
