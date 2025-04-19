package com.maspam.etrain.training.presentation.dashboard.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.data.remote_datasource.local.proto.UserSession
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.domain.model.TrainingModel

@Immutable
data class DashboardState(
    val isLoading: Boolean = true,
    val error: NetworkError? = null,
    val news: List<NewsModel>? = null,
    val openTrain: List<TrainingModel>? = null,
    val user: UserSession? = null,

    val isBottomSheetShow: Boolean? = false,
    val selectedTrain: TrainingModel? = null,
)