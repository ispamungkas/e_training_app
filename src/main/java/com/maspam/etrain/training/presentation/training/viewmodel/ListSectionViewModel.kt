package com.maspam.etrain.training.presentation.training.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.state.ListSectionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListSectionViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource
): ViewModel() {

    private var _state = MutableStateFlow(ListSectionState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getAllSection() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.getAllSection(
                token = userSessionDataSource.getToken(),
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, data = result, isSuccess = true) }
                }
        }
    }

    fun deleteSection(sectionId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.deleteSectionById(
                token = userSessionDataSource.getToken(),
                sectionId = sectionId
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                }

            trainingDataSource.getAllSection(
                token = userSessionDataSource.getToken(),
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, data = result, isSuccess = true) }
                }
        }
    }

    fun setSection(sectionName: String, jp: Int, trainingId: Int) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            trainingDataSource.addSection(
                token = userSessionDataSource.getToken(),
                trainingId = trainingId,
                jp = jp,
                sectionName = sectionName
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                }

            trainingDataSource.getAllSection(
                token = userSessionDataSource.getToken(),
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, data = result, isSuccess = true) }
                }
        }
    }

    fun setInitialValue(sectionList: List<SectionModel>) {
        _state.update {
            it.copy(
                data = sectionList
            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(error = e)
        }
    }

    fun refresh() {
        _state.update { it.copy(isRefresh = true) }
    }

    fun dismissAlert() {
        _state.update { it.copy(isSuccess = false) }
    }

    fun setShowModal(value: Boolean) {
        _state.update { it.copy(showModal = value) }
    }

}