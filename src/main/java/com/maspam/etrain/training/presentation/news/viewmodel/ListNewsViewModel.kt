package com.maspam.etrain.training.presentation.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.news.state.NewsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListNewsViewModel(
    private val newsDataSource: NewsDataSource
): ViewModel() {

    private val _state = MutableStateFlow(NewsState())
    val state = _state
        .onStart {
            getNews()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NewsState()
        )

    private val _event = Channel<GlobalEvent>()
    val event = _event.receiveAsFlow()

    fun getNews() {
        viewModelScope.launch {
            newsDataSource.getAllNews()
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            listNews = result
                        )
                    }
                }
        }
    }

    fun setError(e: NetworkError?) {
        _state.update { it.copy(
            error = e
        ) }
    }
}