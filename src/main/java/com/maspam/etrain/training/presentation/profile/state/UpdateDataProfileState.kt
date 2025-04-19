package com.maspam.etrain.training.presentation.profile.state

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError

data class UpdateDataProfileState(
    val name: String? =  "",
    val nip: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val address: String? = "",
    val gender: String? = "",
    val currentSchool: String? = "",
    val lastStudy: String? = "",
    val dayOfBirth: Long? = null,
    val profileImage: Uri? = null,

    val isLoading: Boolean = false,
    val error: NetworkError? = null,
    val isSuccess: Boolean = false,

)