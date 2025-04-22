package com.maspam.etrain.training.presentation.enroll.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.CertificateModel
import com.maspam.etrain.training.domain.model.EnrollModel

data class EnrollState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val downloadSuccess: Boolean = false,
    val error: NetworkError? = null,
    var data: EnrollModel? = null,
    val certificate: CertificateModel? = null
)
