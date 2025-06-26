package com.maspam.etrain.training.presentation.report

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.UserModel
import java.util.Calendar

data class ReportState (
    val isLoading: Boolean? = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,

    val data: List<UserModel> = listOf(),
    val filteredData : List<UserModel> = listOf(),
    var year: Int = Calendar.getInstance().get(Calendar.YEAR),

    var expanded: Boolean = false,
    val yearRange: List<Int> = (year - 10..year).toList().reversed(),
)