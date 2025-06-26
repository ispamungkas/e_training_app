package com.maspam.etrain.training.presentation.news.state

import androidx.compose.runtime.Immutable
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.domain.model.NewsModel

@Immutable
data class NewsState(
    val isLoading: Boolean = false,
    val error: NetworkError? = null,
    val isRefresh: Boolean = false,

    val listNews: List<NewsModel>? = null,
    val selectedTraining: NewsModel? = null
)