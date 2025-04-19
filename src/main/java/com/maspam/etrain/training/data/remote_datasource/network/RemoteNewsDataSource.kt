package com.maspam.etrain.training.data.remote_datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.ResponseNewsAPI
import com.maspam.etrain.training.data.mapper.toNewsModel
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.domain.model.NewsModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteNewsDataSource(
    private val httpClient: HttpClient
): NewsDataSource {
    private val apikey = "8e891cf9d0e84d3f86b58b29af414566"

    override suspend fun getAllNews(): Result<List<NewsModel>, NetworkError> {
        return safeCall<ResponseNewsAPI> {
            httpClient.get(
                urlString = "https://newsapi.org/v2/everything?q=science&apiKey=${apikey}&language=en&sortBy=popularity&pageSize=30"
            )
        }.map { response ->
            response.articles.map { it.toNewsModel() }
        }
    }

}