package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.data.dto.body.NewsBody
import com.maspam.etrain.training.domain.model.NewsModel

interface NewsDataSource {
    suspend fun getAllNews(): Result<List<NewsModel>, NetworkError>
    suspend fun addNews(newsBody: NewsBody): Result<NewsModel, NetworkError>
    suspend fun updateNews(newsBody: NewsBody): Result<NewsModel, NetworkError>
}