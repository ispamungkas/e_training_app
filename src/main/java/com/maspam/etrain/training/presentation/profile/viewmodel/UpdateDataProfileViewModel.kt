package com.maspam.etrain.training.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.profile.event.UpdateDataProfileFormEvent
import com.maspam.etrain.training.presentation.profile.state.UpdateDataProfileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateDataProfileViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val userDataSource: AuthenticationDataSource,
): ViewModel() {

    private val _state = MutableStateFlow(UpdateDataProfileState())
    val state = _state.asStateFlow()

    private val _event = Channel<UserDataProfileEvent>()
    val event = _event.receiveAsFlow()

    fun onChangeAction(action: UpdateDataProfileFormEvent) {
        when (action) {
            is UpdateDataProfileFormEvent.AddressChange -> { _state.update { it.copy(address = action.address) } }
            is UpdateDataProfileFormEvent.CurrentSchoolChange -> { _state.update { it.copy(currentSchool = action.school) } }
            is UpdateDataProfileFormEvent.DayOfBirthChange -> { _state.update { it.copy(dayOfBirth = action.dob) } }
            is UpdateDataProfileFormEvent.EmailChange -> { _state.update { it.copy(email = action.email) } }
            is UpdateDataProfileFormEvent.GenderChange -> { _state.update { it.copy(gender = action.gender) } }
            is UpdateDataProfileFormEvent.LastStudyChange -> { _state.update { it.copy(lastStudy = action.school) } }
            is UpdateDataProfileFormEvent.NameChange -> { _state.update { it.copy(name = action.name) } }
            is UpdateDataProfileFormEvent.PhoneNumberChange -> { _state.update { it.copy(phoneNumber = action.phoneNumber) } }
            is UpdateDataProfileFormEvent.ProfileImageChange -> { _state.update { it.copy(profileImage = action.profile) } }
            is UpdateDataProfileFormEvent.Submit -> submit(userModel = action.userModel)
        }
    }

    fun submit(userModel: UserModel) {
        setupState(userModel)
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            userDataSource.changeDataProfile(
                token = userSessionDataSource.getToken(),
                state.value,
                id = userSessionDataSource.getId()
            )
                .onError { e ->
                    _state.update { it.copy(
                        isLoading = false,
                    ) }
                    _event.send(
                        UserDataProfileEvent.Error(e)
                    )
                }
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = true
                    ) }
                    _event.send(
                        UserDataProfileEvent.Success
                    )
                }
        }
    }

    private fun setupState(userModel: UserModel) {
        _state.update { it.copy(
            name = if (it.name.isNullOrEmpty()) userModel.name else it.name,
            currentSchool = if (it.currentSchool.isNullOrEmpty()) userModel.currentSchool else it.currentSchool,
            lastStudy = if (it.lastStudy.isNullOrEmpty()) userModel.lastEducation else it.lastStudy,
            gender = if (it.gender.isNullOrEmpty()) userModel.gender else it.gender,
            dayOfBirth = if (it.dayOfBirth == 0L) userModel.dayOfBirth else it.dayOfBirth,
            email = if (it.email.isNullOrEmpty()) userModel.email else it.email,
            phoneNumber = if (it.phoneNumber.isNullOrEmpty()) userModel.phoneNumber else it.phoneNumber,
            nip = if (it.nip.isNullOrEmpty()) userModel.nip else it.nip,
            address = if (it.address.isNullOrEmpty()) userModel.address else it.address
        ) }
    }

    fun setError(e: NetworkError?) {
        _state.update { it.copy(error = e) }
    }

    fun setSuccess(b: Boolean) {
        _state.update { it.copy(isSuccess = b) }
    }

    sealed interface UserDataProfileEvent {
        data class Error(val e: NetworkError): UserDataProfileEvent
        object Success: UserDataProfileEvent
    }

}