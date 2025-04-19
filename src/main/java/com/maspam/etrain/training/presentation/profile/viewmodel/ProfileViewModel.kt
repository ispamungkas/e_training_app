package com.maspam.etrain.training.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.profile.state.ProfileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val authenticationDataSource: AuthenticationDataSource,
    private val enrollDataSource: EnrollDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            getUser()
            getEnroll()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProfileState(isLoading = true)
        )

    private val _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getUser() {
        viewModelScope.launch {
            authenticationDataSource.getUser(userSessionDataSource.getId())
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            user = result,
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e
                        )
                    }
                }
        }
    }

    fun getEnroll() {
        viewModelScope.launch {
            enrollDataSource.getEnrollById(
                token = userSessionDataSource.getToken(),
                id = userSessionDataSource.getId()
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            totalJp = countTotalJp(result),
                            trainJp = countTrainJp(result),
                            workShopJp =countWorkshopJp(result)
                        )
                    }
                }
                .onError { e ->
                    _state.update { it.copy(
                        isLoading = false,
                        error = e
                    ) }
                }
        }
    }

    fun removeSession() {
        viewModelScope.launch {
            userSessionDataSource.delete()
        }
    }

    private fun countTotalJp(data: List<EnrollModel>): Int {
        var count = 0
        data.forEach { enrollModel -> count += enrollModel.totalJp ?: 0 }
        return count
    }

    private fun countTrainJp(data: List<EnrollModel>): Int {
        var count = 0
        data.filter { it.trainingDetail?.typeTraining?.contains("training") ?: false }
            .forEach { enrollModel -> count += enrollModel.totalJp ?: 0 }
        return count
    }

    private fun countWorkshopJp(data: List<EnrollModel>): Int {
        var count = 0
        data.filter { it.trainingDetail?.typeTraining?.contains("webinar") ?: false }
            .forEach { enrollModel -> count += enrollModel.totalJp ?: 0 }
        return count
    }

}