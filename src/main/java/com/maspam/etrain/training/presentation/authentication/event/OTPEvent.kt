package com.maspam.etrain.training.presentation.authentication.event

import com.maspam.etrain.training.core.domain.utils.NetworkError

sealed interface OTPEvent {
    data class Error(val e: NetworkError): OTPEvent
    data class Success(val message: String): OTPEvent
}