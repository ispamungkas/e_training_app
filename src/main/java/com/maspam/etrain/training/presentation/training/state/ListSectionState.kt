package com.maspam.etrain.training.presentation.training.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.SectionModel

data class ListSectionState(
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,
    val showModal: Boolean = false,

    val data: List<SectionModel>? = null
)