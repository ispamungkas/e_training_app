package com.maspam.etrain.training.presentation.dashboard.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.UserModel

@Immutable
data class UserAccountState(
    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val isBottomSheetShow: Boolean = false,
    val error: NetworkError? = null,
    val data: List<UserModel>? = null,
    val filteredData: List<UserModel>? = null,
    val selectedNip: String? = "",

    val name: String? = null,
    val isHead: Boolean? = false,
    val nip: String? = null,
    val nameError: String? = null,
    val nipError: String? = null
)
