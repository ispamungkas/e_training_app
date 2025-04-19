package com.maspam.etrain.training.data.remote_datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.EnrollDto
import com.maspam.etrain.training.data.mapper.toEnrollModel
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.model.EnrollModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteEnrollDataSource(
    private val httpClient: HttpClient
): EnrollDataSource {
    override suspend fun enrollTraining(
        token: String,
        trainingId: Int,
        userId: Int
    ): Result<String, NetworkError> {
        val body = mapOf(
            "train" to trainingId,
            "user" to userId
        )

        return safeCall <BaseDto<String>>{
            httpClient.post(
                urlString = constructUrl("/enroll/")
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.message ?: ""
        }
    }

    override suspend fun getAllEnroll(token: String): Result<List<EnrollModel>, NetworkError> {
        return safeCall <BaseDto<List<EnrollDto>>>{
            httpClient.get(
                urlString = constructUrl("/enroll/")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toEnrollModel() } ?: emptyList()
        }
    }

    override suspend fun getEnrollById(
        token: String,
        id: Int
    ): Result<List<EnrollModel>, NetworkError> {
        return safeCall <BaseDto<List<EnrollDto>>>{
            httpClient.get(
                urlString = constructUrl("/enroll/?user=$id")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toEnrollModel() } ?: emptyList()
        }
    }
}