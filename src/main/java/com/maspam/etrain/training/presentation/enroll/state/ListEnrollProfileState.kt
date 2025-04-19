package com.maspam.etrain.training.presentation.enroll.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.EnrollModel

@Immutable
data class ListEnrollProfileState(
    val isLoading: Boolean = false,
    val error: NetworkError? = null,
    val data: List<EnrollModel>? = null,
    val filteredData: List<EnrollModel>? = data
)
