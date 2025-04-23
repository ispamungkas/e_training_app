package com.maspam.etrain.training.data.remote_datasource.network


import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.SectionDto
import com.maspam.etrain.training.data.dto.TrainingDto
import com.maspam.etrain.training.data.dto.body.SectionBody
import com.maspam.etrain.training.data.dto.body.TrainingBody
import com.maspam.etrain.training.data.mapper.toSectionModel
import com.maspam.etrain.training.data.mapper.toTrainingModel
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TrainingModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

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

    override suspend fun getOpenTraining(token:String): Result<List<TrainingModel>, NetworkError> {
        return safeCall<BaseDto<List<TrainingDto>>> {
            httpClient.get(
                urlString = constructUrl("/training/?publish=true")
            ){
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toTrainingModel() } ?: emptyList()
        }
    }

    override suspend fun getTrainingById(
        token: String,
        trainingId: Int
    ): Result<TrainingModel, NetworkError> {
        return safeCall<BaseDto<TrainingDto>> {
            httpClient.get(
                urlString = constructUrl("/training/?id=$trainingId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.toTrainingModel() ?: TrainingModel()
        }
    }

    override suspend fun addTraining(
        token: String,
        trainingBody: TrainingBody
    ): Result<TrainingModel, NetworkError> {
        return safeCall <BaseDto<TrainingDto>>{
            httpClient.post(
                urlString = constructUrl("/section/")
            ) {
                bearerAuth(token = token)
                setBody(trainingBody)
            }
        }.map { response ->
            response.data?.toTrainingModel() ?: TrainingModel()
        }
    }

    override suspend fun setPublishedTraining(
        token: String,
        trainingId: Int,
        isPublish: Boolean
    ): Result<TrainingModel, NetworkError> {
        val body = mapOf(
            "is_publish" to isPublish
        )
        return safeCall<BaseDto<TrainingDto>> {
            httpClient.patch(
                urlString = constructUrl("/training/$trainingId")
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toTrainingModel() ?: TrainingModel()
        }
    }

    override suspend fun getAllSection(token: String): Result<List<SectionModel>, NetworkError> {
        return safeCall<BaseDto<List<SectionDto>>> {
            httpClient.get(
                urlString = constructUrl("/section/")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toSectionModel() } ?: emptyList()
        }
    }

    override suspend fun getSectionByID(
        token: String,
        sectionId: Int
    ): Result<SectionModel, NetworkError> {
        return safeCall<BaseDto<SectionDto>> {
            httpClient.get(
                urlString = constructUrl("/section/?id=$sectionId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.toSectionModel() ?: SectionModel()
        }
    }

    override suspend fun deleteSectionById(
        token: String,
        sectionId: Int
    ): Result<String, NetworkError> {
        return safeCall<BaseDto<String>> {
            httpClient.delete(
                urlString = constructUrl("/section/$sectionId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data ?: ""
        }
    }

    override suspend fun addSection(
        token: String,
        trainingId: Int,
        jp: Int,
        sectionName: String
    ): Result<SectionModel, NetworkError> {
        val body = SectionBody(
            trainId = trainingId,
            jp = jp,
            name = sectionName
        )
        return safeCall <BaseDto<SectionDto>>{
            httpClient.post(
                urlString = constructUrl("/section/")
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toSectionModel() ?: SectionModel()
        }
    }


}