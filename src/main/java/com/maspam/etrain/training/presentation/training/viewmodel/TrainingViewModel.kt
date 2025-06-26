package com.maspam.etrain.training.presentation.training.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.core.presentation.utils.formValidation.CommonValidation
import com.maspam.etrain.training.data.dto.body.TrainingBody
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.TrainingModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.training.event.FormTrainingEvent
import com.maspam.etrain.training.presentation.training.state.TrainingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val userSessionDataSource: UserSessionDataSource,
    private val trainingDataSource: TrainingDataSource,
    context: Context
) : ViewModel() {

    private val commonValidation: CommonValidation =
        CommonValidation(context = context, fieldNameValidation = "field")

    private var _state = MutableStateFlow(TrainingState())
    val state = _state
        .onStart {
            getListTraining()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            TrainingState()
        )

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun setInitialValue(value: TrainingModel) {
        _state.update {
            it.copy(
                selectedTraining = value,
                name = value.name ?: "",
                dateLine = value.due ?: 0L,
                attend = value.attend ?: 0L,
                desc = value.desc ?: ""
            )
        }
    }

    fun onChangeAction(action: FormTrainingEvent) {
        when (action) {
            is FormTrainingEvent.AttendChange -> _state.update { it.copy(attend = action.attend) }
            is FormTrainingEvent.DatelineChange -> _state.update { it.copy(dateLine = action.dateline) }
            is FormTrainingEvent.DescriptionChange -> _state.update { it.copy(desc = action.description) }
            is FormTrainingEvent.ImgChange -> _state.update { it.copy(img = action.img) }
            is FormTrainingEvent.NameChange -> _state.update { it.copy(name = action.name) }
            is FormTrainingEvent.Submit -> {
                submitForm(action.trainingBody)
            }

            is FormTrainingEvent.Update -> {
                updateForm(action.trainingBody)
            }
        }
    }

    private fun submitForm(trainingBody: TrainingBody) {

        val name = commonValidation.execute(trainingBody.name ?: "")
        val attendance = commonValidation.execute(trainingBody.attend.toString())
        val due = commonValidation.execute(trainingBody.dateline.toString())
        val description = commonValidation.execute(trainingBody.desc ?: "")
        val uri = commonValidation.execute(trainingBody.img.toString())

        val hasError = listOf(
            name, attendance, due, description, uri
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                nameError = name.errorMessage ?: "",
                attendError = attendance.errorMessage ?: "",
                uriError = uri.errorMessage ?: "",
                dateLineError = due.errorMessage ?: "",
                descError = description.errorMessage ?: "",
            )
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.addTraining(
                token = userSessionDataSource.getToken(),
                trainingBody = trainingBody
            )
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
        }

    }

    private fun updateForm(trainingBody: TrainingBody) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.updateTraining(
                token = userSessionDataSource.getToken(),
                trainingBody = trainingBody,
                trainingId = _state.value.selectedTraining?.id ?: 0
            )
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
        }

    }

    fun getListTraining() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            trainingDataSource.getAllTraining(
                token = userSessionDataSource.getToken()
            )
                .onError { error ->
                    _state.update { it.copy(isLoading = false, isRefresh = false, error = error) }
                    _globalEvent.send(GlobalEvent.Error(e = error))
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefresh = false,
                            listTraining = result,
                            filteredList = result
                        )
                    }
                }
        }
    }

    fun setValueOfSearch(value: String) {
        if (value.isEmpty() || value == "") {
            _state.update {
                it.copy(
                    search = value,
                    filteredList = it.listTraining
                )
            }
        } else {
            _state.update {
                it.copy(
                    search = value,
                    filteredList = it.filteredList?.filter { dataFilter ->
                        dataFilter.name?.contains(value, true) == true
                    }
                )
            }
        }
    }

    fun setFilter(value: Boolean?) {
        _state.update { state ->
            state.copy(
                filterByStatus = value,
                filteredList = value?.let {
                    state.listTraining?.filter { data ->
                        data.isOpen == value
                    }
                } ?: state.filteredList
            )
        }
    }

    fun setFilterAll() {
        _state.update { state ->
            state.copy(
                filteredList = _state.value.listTraining
            )
        }
    }

    fun setFilterPublish(value: Boolean) {
        _state.update { state ->
            state.copy(
                filterByStatus = value,
                filteredList = value.let {
                    state.listTraining?.filter { data ->
                        data.isPublish == value
                    }
                } ?: state.filteredList
            )
        }
    }

    fun refresh() {
        _state.update {
            it.copy(
                isRefresh = true
            )
        }
    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(error = e)
        }
    }

    fun showConfirmationModal(value: Boolean) {
        _state.update {
            it.copy(showConfirmationModal = value)
        }
    }

    fun setSelectedTraining(trainingModel: TrainingModel) {
        _state.update {
            it.copy(selectedTraining = trainingModel)
        }
    }
}