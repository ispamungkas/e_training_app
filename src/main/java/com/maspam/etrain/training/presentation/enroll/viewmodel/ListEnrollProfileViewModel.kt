package com.maspam.etrain.training.presentation.enroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.presentation.enroll.state.ListEnrollProfileState
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListEnrollProfileViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val enrollDataSource: EnrollDataSource
) : ViewModel() {

    private var _state = MutableStateFlow(ListEnrollProfileState())
    val state = _state
        .onStart {
            getEnroll()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ListEnrollProfileState()
        )

    private val _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getEnroll() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            enrollDataSource.getEnrollById(
                token = userSessionDataSource.getToken(),
                id = userSessionDataSource.getId()
            )
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result.sortedByDescending { enroll -> enroll.id },
                            filteredData = result.sortedByDescending { enroll -> enroll.id }
                        )
                    }
                }
        }
    }

    fun setFilter(filter: String) {
        if (!filter.lowercase().contains("all")) {
            val x = filter.lowercase()
            _state.update {
                it.copy(
                    filteredData = it.data?.filter { enrollModel -> enrollModel.status?.contains(x) == true }?.sortedByDescending { enroll -> enroll.id }
                )
            }
        } else {
            _state.update {
                it.copy(
                    filteredData = it.data?.sortedByDescending { enroll -> enroll.id }
                )
            }
        }
    }


    fun setError(e: NetworkError?) {
        _state.update { it.copy(error = e) }
    }

}