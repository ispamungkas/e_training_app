package com.maspam.etrain.training.core.domain.utils

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    BAD_REQUEST,
    NO_INTERNET,
    NOT_ACCEPTED,
    SERVER_ERROR,
    TOO_MANY_REQUEST,
    NOT_FOUND,
    SERIALIZATION,
    UNKNOWN,
}