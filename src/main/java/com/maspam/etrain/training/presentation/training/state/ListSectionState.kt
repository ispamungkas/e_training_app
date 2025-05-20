package com.maspam.etrain.training.presentation.training.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.PostTestModel
import com.maspam.etrain.training.domain.model.SectionModel

@Immutable
data class ListSectionState(
    val trainId: Int? = null,
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val isEdit: Boolean = false,
    val error: NetworkError? = null,
    val showModal: Boolean = false,
    val showConfirmation: Boolean = false,
    val postTests: List<PostTestModel> = emptyList(),

    val initialName: String? = "",
    val initialJp: Int? = null,
    val selectedSection: Int? = 0,

    val data: List<SectionModel>? = null
)