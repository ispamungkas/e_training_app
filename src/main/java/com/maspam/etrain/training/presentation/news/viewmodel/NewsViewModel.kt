package com.maspam.etrain.training.presentation.news.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.onError
import com.maspam.etrain.training.core.domain.utils.onSuccess
import com.maspam.etrain.training.core.presentation.utils.formValidation.CommonValidation
import com.maspam.etrain.training.data.dto.body.NewsBody
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.domain.model.NewsModel
import com.maspam.etrain.training.presentation.global.event.GlobalEvent
import com.maspam.etrain.training.presentation.news.event.FormNewsEvent
import com.maspam.etrain.training.presentation.news.state.NewsCalState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class NewsViewModel (
    private val newsDataSource: NewsDataSource,
    private val userSessionDataSource: UserSessionDataSource,
    context: Context
): ViewModel() {
    private val commonValidation: CommonValidation =
        CommonValidation(context = context, fieldNameValidation = "field")

    private var _state = MutableStateFlow(NewsCalState())
    val state = _state.asStateFlow()

    private var _globalEvent = Channel<GlobalEvent>()
    val globalEvent = _globalEvent.receiveAsFlow()

    fun setInitialValue(value: NewsModel) {
        _state.update {
            it.copy(
                author = value.author ?: "",
                name = value.name ?: "",
                publishDate = value.publishDate ?: 0L,
                desc = value.desc ?: ""
            )
        }
    }

    fun onChangeAction(action: FormNewsEvent) {
        when (action) {
            is FormNewsEvent.AuthorChange -> _state.update { it.copy(author = action.author) }
            is FormNewsEvent.DescChange -> _state.update { it.copy(desc = action.desc) }
            is FormNewsEvent.ImgChange -> _state.update { it.copy(img = action.img) }
            is FormNewsEvent.NameChange -> _state.update { it.copy(name = action.name) }
            is FormNewsEvent.Submit -> {
                submitForm(action.newsBody)
            }
            is FormNewsEvent.Update -> {
                updateForm(action.newsBody)
            }
        }
    }

    private fun submitForm(newsBody: NewsBody) {

        val date = Calendar.getInstance().timeInMillis

        val name = commonValidation.execute(newsBody.name ?: "")
        val desc = commonValidation.execute(newsBody.desc ?: "")
        val publishDate = date
        val uri = commonValidation.execute(newsBody.image.toString())

        val hasError = listOf(
            name, desc, uri
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                nameError = name.errorMessage ?: "",
                descError = desc.errorMessage ?: "",
                imgError = uri.errorMessage ?: "",
            )
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val user = userSessionDataSource.getUserSession().name

            newsDataSource.addNews(
                newsBody = newsBody.copy(
                    publishDate = publishDate,
                    author = user
                )
            )
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { _ ->
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }

            println("selesai")
        }

    }

    private fun updateForm(newsBody: NewsBody) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val user = userSessionDataSource.getUserSession().name

            newsDataSource.updateNews(
                newsBody = newsBody.copy(
                    id = newsBody.id,
                    author = user
                ),
            )
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _globalEvent.send(GlobalEvent.Error(e))
                }
                .onSuccess { _ ->
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
        }

    }

    fun setError(e: NetworkError?) {
        _state.update {
            it.copy(
                error = e
            )
        }
    }

    fun showConfirmationModal(value: Boolean) {
        _state.update {
            it.copy(
                userConfirmation = value
            )
        }
    }
}