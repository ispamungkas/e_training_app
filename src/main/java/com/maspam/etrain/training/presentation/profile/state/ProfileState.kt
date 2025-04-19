package com.maspam.etrain.training.presentation.profile.state

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.UserModel

data class ProfileState(
    val isLoading: Boolean = true,
    val error: NetworkError? = null,
    val totalJp: Int? = 0,
    val trainJp: Int? = 0,
    val workShopJp: Int? = 0,
    val user: UserModel? = null,
    val enroll: List<EnrollModel>? = null
)
