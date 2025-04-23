package com.maspam.etrain.training.presentation.training.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.TrainingModel

@Immutable
data class TrainingState(
    val isLoading: Boolean? = false,
    val isRefresh: Boolean = false,
    val error: NetworkError? = null,

    val search: String? = "",
    val filterByStatus: Boolean? = null,

    val listTraining: List<TrainingModel>? = emptyList(),
    val filteredList: List<TrainingModel>? = listTraining,
    val selectedTraining: TrainingModel? = null
)