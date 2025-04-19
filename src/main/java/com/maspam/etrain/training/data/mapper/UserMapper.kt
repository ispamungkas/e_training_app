package com.maspam.etrain.training.data.mapper

import com.maspam.etrain.training.data.dto.BaseDtoUser
import com.maspam.etrain.training.data.dto.UserDto
import com.maspam.etrain.training.domain.model.UserModel

fun BaseDtoUser<UserDto>.toUserModel(): UserModel {
    return UserModel(
        id = this.data?.id,
        name = this.data?.name,
        token = this.token,
        address = this.data?.address,
        email = this.data?.email,
        phoneNumber = this.data?.pNumber,
        gender = this.data?.gender,
        nip = this.data?.nip,
        lastEducation = this.data?.lEducation,
        currentSchool = this.data?.cSchool,
        isHead = this.data?.role.toBoolean(),
        imageProfile = this.data?.imgProfile,
    )
}

fun UserDto.toUserModel(): UserModel {
    return UserModel(
        id = this.id,
        name = this.name,
        address = this.address,
        email = this.email,
        phoneNumber = this.pNumber,
        gender = this.gender,
        nip = this.nip,
        lastEducation = this.lEducation,
        currentSchool = this.cSchool,
        dayOfBirth = this.dob,
        isHead = this.role.toBoolean(),
        imageProfile = this.imgProfile,
    )
}