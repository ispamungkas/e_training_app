package com.maspam.etrain.training.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int? = null,
    val token: String? = null,
    val name: String? = null,
    val nip: String? = null,
    val password: String? = null,
    val email: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val role: String? = null,
    val lastEducation: String? = null,
    val currentSchool: String? = null,
    val dayOfBirth: Long? = null,
    val imageProfile: String? = null,
    val enrolls: List<EnrollModel>? = null
)
