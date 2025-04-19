package com.maspam.etrain.training.data.remote_datasource.network


import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.TrainingDto
import com.maspam.etrain.training.data.mapper.toTrainingModel
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.TrainingModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class RemoteTrainingDataSource(
    private val httpClient: HttpClient
) : TrainingDataSource {
    override suspend fun getAllTraining(token: String): Result<List<TrainingModel>, NetworkError> {
        return safeCall<BaseDto<List<TrainingDto>>> {
            httpClient.get(
                urlString = constructUrl("/training/")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toTrainingModel() } ?: emptyList()
        }
    }

    override suspend fun getOpenTraining(token:String, isPublish: Boolean): Result<List<TrainingModel>, NetworkError> {
        return safeCall<BaseDto<List<TrainingDto>>> {
            httpClient.get(
                urlString = constructUrl("/training/?publish=${if (isPublish) "True" else "False"}")
            ){
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toTrainingModel() } ?: emptyList()
        }
    }


}