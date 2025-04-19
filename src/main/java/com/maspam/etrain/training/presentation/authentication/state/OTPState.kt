package com.maspam.etrain.training.presentation.authentication.state

import com.maspam.etrain.training.core.domain.utils.NetworkError


data class OTPState(
    val isLoading: Boolean? = false,
    val error: NetworkError? = null,
    val code: List<Int?> = (1..4).map { null },
    val focusIndex: Int? = null,
)
