package com.maspam.etrain.training.presentation.profile.event

import android.net.Uri
import com.maspam.etrain.training.domain.model.UserModel

sealed class UpdateDataProfileFormEvent {
    data class NameChange(val name: String): UpdateDataProfileFormEvent()
    data class EmailChange(val email: String): UpdateDataProfileFormEvent()
    data class GenderChange(val gender: String): UpdateDataProfileFormEvent()
    data class CurrentSchoolChange(val school: String): UpdateDataProfileFormEvent()
    data class LastStudyChange(val school: String): UpdateDataProfileFormEvent()
    data class DayOfBirthChange(val dob: Long?): UpdateDataProfileFormEvent()
    data class PhoneNumberChange(val phoneNumber: String): UpdateDataProfileFormEvent()
    data class AddressChange(val address: String): UpdateDataProfileFormEvent()
    data class ProfileImageChange(val profile: Uri): UpdateDataProfileFormEvent()

    data class Submit(val userModel: UserModel) : UpdateDataProfileFormEvent()
}