package com.maspam.etrain.training.core.networking

import com.maspam.etrain.BuildConfig

fun constructUrl(url: String): String {
    return when{
        url.contains(BuildConfig.BASE_URL) -> BuildConfig.BASE_URL
        url.startsWith('/') -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url.drop(1)
    }
}
