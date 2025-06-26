package com.maspam.etrain.training.presentation.report

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class ReportViewModel(
    private val authenticationDataSource: AuthenticationDataSource
): ViewModel() {
    private var _state = MutableStateFlow(ReportState())
    val state = _state
        .onStart {
            getAllUser()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ReportState()
        )

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun getAllUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authenticationDataSource.getAllUser()
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, error = error) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            data = result,
                            filteredData = result
                        )
                    }
                }
        }
    }

    fun expanded(isExpanded: Boolean) {
        _state.update {
            it.copy(
                expanded = true
            )
        }
    }

    @SuppressLint("NewApi")
    fun setYear(year: Int) {
        _state.update {
            it.copy(
                year = year,
                expanded = false,
                filteredData = it.data.map { userModel ->
                    val enroll = userModel.enrolls
                    val filteredEnroll = enroll?.filter { enrollModel ->
                        enrollModel.trainingDetail?.createdAt?.let { createdAt ->
                            val dateInMillis = createdAt * 1000
                            val instant = Instant.ofEpochMilli(dateInMillis)
                            val zone = ZoneId.systemDefault()
                            val currentYear = instant.atZone(zone).toLocalDate().year
                            currentYear == year
                        } ?: false
                    }

                    userModel.copy(
                        enrolls = filteredEnroll
                    )
                }
            )
        }
    }

    @SuppressLint("NewApi")
    fun getUserByCondition(condition: String) {
        println(condition)
        _state.update {
            it.copy(
                filteredData = if (condition == "Inactive User") {
                    it.filteredData.sortedBy { userModel -> userModel.enrolls?.size }
                } else if (condition == "Active User") {
                    it.filteredData.sortedBy { userModel -> userModel.enrolls?.size }.reversed()
                }
                else {
                    it.data
                }

            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                error = e
            )
        }
    }

    fun refresh() {
        _state.update {
            it.copy(
                isRefresh = true
            )
        }
        viewModelScope.launch {
            getAllUser()
        }
    }
}