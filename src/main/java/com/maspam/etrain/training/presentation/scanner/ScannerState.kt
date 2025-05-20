package com.maspam.etrain.training.presentation.scanner

import com.maspam.etrain.training.core.domain.utils.NetworkError

data class ScannerState(
    val isLoading: Boolean? = false,
    val isFirst: Boolean? = true,
    val isError: NetworkError? = null,
    val isSuccess: Boolean? = null,
    val isVerified: Boolean? = null,
    val isFailed: Boolean? = null,
)
