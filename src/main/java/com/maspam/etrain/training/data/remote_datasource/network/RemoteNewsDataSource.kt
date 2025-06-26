package com.maspam.etrain.training.data.remote_datasource.network

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.core.presentation.utils.FileInfo
import com.maspam.etrain.training.core.presentation.utils.FileReader
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.body.NewsBody
import com.maspam.etrain.training.domain.datasource.network.NewsDataSource
import com.maspam.etrain.training.domain.model.NewsModel
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class RemoteNewsDataSource(
    private val httpClient: HttpClient,
    private val fileReader: FileReader
): NewsDataSource {
//    private val apikey = "8e891cf9d0e84d3f86b58b29af414566"

//    override suspend fun getAllNews(): Result<List<NewsModel>, NetworkError> {
//        return safeCall<ResponseNewsAPI> {
//            httpClient.get(
//                urlString = "https://newsapi.org/v2/everything?q=science&apiKey=${apikey}&language=en&sortBy=popularity&pageSize=30"
//            )
//        }.map { response ->
//            response.articles.map { it.toNewsModel() }
//        }
//    }

    override suspend fun getAllNews(): Result<List<NewsModel>, NetworkError> {
        return safeCall<BaseDto<List<NewsModel>>> {
            httpClient.get(
                urlString = constructUrl("/news/")
            )
        }.map { response ->
            response.data ?: emptyList()
        }
    }

    override suspend fun addNews(newsBody: NewsBody): Result<NewsModel, NetworkError> {
        var info : FileInfo? = null
        if (newsBody.image.toString().isNotEmpty()) {
            info = newsBody.image.let { fileReader.uriToFileInfo(it ?: Uri.parse("") ) }
        }

        return safeCall <BaseDto<NewsModel>>{
            httpClient.post(
                urlString = constructUrl("/news/")
            ) {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", newsBody.name ?: "")
                        append("desc", newsBody.desc ?: "")
                        append("author", newsBody.author ?: "")
                        append("publish_date", newsBody.publishDate ?: 0L)
                        info?.let {
                            append("img", info.bytes, Headers.build {
                                append(HttpHeaders.ContentType, info.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                            })
                        }
                    }
                ))
            }
        }.map { response ->
            response.data ?: NewsModel()
        }
    }

    override suspend fun updateNews(newsBody: NewsBody): Result<NewsModel, NetworkError> {
        var info : FileInfo? = null
        if (newsBody.image.toString().isNotEmpty()) {
            info = newsBody.image.let { fileReader.uriToFileInfo(it ?: Uri.parse("") ) }
        }

        return safeCall <BaseDto<NewsModel>>{
            httpClient.patch(
                urlString = constructUrl("/news/${newsBody.id}")
            ) {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", newsBody.name ?: "")
                        append("desc", newsBody.desc ?: "")
                        append("author", newsBody.author ?: "")
                        append("publish_date", newsBody.publishDate ?: 0L)
                        info?.let {
                            append("img", info.bytes, Headers.build {
                                append(HttpHeaders.ContentType, info.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                            })
                        }
                    }
                ))
            }
        }.map { response ->
            response.data ?: NewsModel()
        }
    }

}