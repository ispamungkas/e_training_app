package com.maspam.etrain.training.presentation.training.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.TrainingModel

@Immutable
data class TrainingState(
    val isLoading: Boolean? = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,

    val search: String? = "",
    val filterByStatus: Boolean? = null,

    val listTraining: List<TrainingModel>? = emptyList(),
    val filteredList: List<TrainingModel>? = listTraining,
    val selectedTraining: TrainingModel? = null,

    /**
     * Form input
     */
    val name: String = "",
    val desc: String = "",
    val dateLine: Long = 0L,
    val attend: Long = 0L,
    val img: Uri = Uri.parse(""),
    val showConfirmationModal: Boolean = false,

    val nameError: String = "",
    val descError: String = "",
    val dateLineError: String = "",
    val attendError: String = "",
    val uriError: String = ""
)