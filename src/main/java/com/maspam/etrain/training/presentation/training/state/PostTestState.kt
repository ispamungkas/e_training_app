package com.maspam.etrain.training.presentation.training.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.TrainingModel

@Immutable
data class PostTestState(
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isSuccessful: Boolean = false,
    val error: NetworkError? = null,
    val data: TrainingModel? = null,

    val question1: String = "",
    val question2: String = "",
    val question3: String = "",

)