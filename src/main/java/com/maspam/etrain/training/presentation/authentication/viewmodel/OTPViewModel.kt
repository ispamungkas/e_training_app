package com.maspam.etrain.training.presentation.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.presentation.authentication.OTPAction
import com.maspam.etrain.training.presentation.authentication.event.OTPEvent
import com.maspam.etrain.training.presentation.authentication.state.OTPState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OTPViewModel(
    private val authenticationDataSource: AuthenticationDataSource
): ViewModel() {

    private var _state = MutableStateFlow(OTPState())
    val state = _state.asStateFlow()

    private val _eventOTP = Channel<OTPEvent>()
    val eventOtp = _eventOTP.receiveAsFlow()


    fun onAction(action: OTPAction){
        when(action) {
            is OTPAction.OnChangeFieldFocused -> {
                _state.update { it.copy(
                    focusIndex = action.index
                ) }
            }
            is OTPAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            OTPAction.OnKeyboardBack -> {
                val previousIndex = getPreviousIndex(_state.value.focusIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) {
                                null
                            } else {
                                number
                            }
                        },
                        focusIndex = previousIndex
                    )
                }
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }

        val wasNumberRemove = number == null
        _state.update {
            it.copy(
                code = newCode,
                focusIndex = if (wasNumberRemove || it.code.getOrNull(index) != null) {
                    it.focusIndex
                } else {
                    getNextFocusTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusIndex
                    )
                }
            )
        }
    }

    private fun getPreviousIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }

        if (currentFocusedIndex == 3) {
            return currentFocusedIndex
        }

        return getFirstEmptyIndexAfterFocusedIndex(
            code = currentCode,
            currentIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyIndexAfterFocusedIndex(
        code: List<Int?>,
        currentIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentIndex
    }

    fun verifyOtp(otp: String){
        _state.update { it.copy(
            isLoading = true,
        )}

        viewModelScope.launch {
            authenticationDataSource.verifyOtp(otp)
                .onSuccess { res ->
                    _state.update { it.copy(
                        isLoading = false,
                    )}
                    _eventOTP.send(OTPEvent.Success(message = res))
                }
                .onError { networkError ->
                    _state.update { it.copy(
                        isLoading = false,
                    ) }

                    _eventOTP.send(OTPEvent.Error(e = networkError))
                }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update { it.copy(
            error = e
        ) }
    }

}