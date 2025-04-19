package com.maspam.etrain.training.presentation.global.event

import com.maspam.etrain.training.core.domain.utils.NetworkError

sealed interface GlobalEvent {
    data class Error(val e: NetworkError): GlobalEvent
}