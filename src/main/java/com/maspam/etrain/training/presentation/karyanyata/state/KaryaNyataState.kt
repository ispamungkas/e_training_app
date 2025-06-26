package com.maspam.etrain.training.presentation.karyanyata.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.KaryaNyataModel
import com.maspam.etrain.training.domain.model.UserModel

@Immutable
data class KaryaNyataState(
    val isLoading: Boolean? = false,
    val isRefresh: Boolean = false,
    val isSuccess: Boolean = false,
    val error: NetworkError? = null,
    var grade: String? = "",

    val enroll: List<EnrollModel>? = null,
    val karyaNyata: KaryaNyataModel? = null,
    val user: UserModel? = null
)