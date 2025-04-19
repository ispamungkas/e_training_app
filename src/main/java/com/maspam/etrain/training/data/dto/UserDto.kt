package com.maspam.etrain.training.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int?,
    val token: String? = "",
    val name: String?,
    val nip: String?,
    val email: String?,
    val address: String?,

    @SerialName("p_number")
    val pNumber: String?,

    @SerialName("l_edu")
    val lEducation: String?,

    @SerialName("c_school")
    val cSchool: String?,

    @SerialName("img_profile")
    val imgProfile: String?,

    val gender: String?,
    val role: String?,
    val enrolls: List<EnrollDto>?,

    @SerialName("created_at")
    val createdAt: Int?,

    val dob: Long?,

    @SerialName("updated_at")
    val updatedAt: Long?,

    @SerialName("deleted_at")
    val deletedAt: Long?
)
