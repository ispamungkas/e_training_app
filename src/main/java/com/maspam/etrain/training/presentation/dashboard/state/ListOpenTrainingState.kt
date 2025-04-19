package com.maspam.etrain.training.presentation.dashboard.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.TrainingModel

@Immutable
data class ListOpenTrainingState (
    val isLoading: Boolean = false,
    val isBottomSheetShow: Boolean = false,
    val selectedTrain: TrainingModel? = null,
    val error: NetworkError? =  null,
    val data: List<TrainingModel>? = null,
)