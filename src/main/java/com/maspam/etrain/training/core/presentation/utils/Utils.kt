package com.maspam.etrain.training.core.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateTimeFormatter(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("d - M - yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.toDateTimeVersion2Formatter(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
    return format.format(date)
}