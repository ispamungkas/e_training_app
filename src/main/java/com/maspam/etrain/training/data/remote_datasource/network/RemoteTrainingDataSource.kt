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
import com.maspam.etrain.training.data.dto.SectionDto
import com.maspam.etrain.training.data.dto.TopicDto
import com.maspam.etrain.training.data.dto.TrainingDto
import com.maspam.etrain.training.data.dto.body.SectionBody
import com.maspam.etrain.training.data.dto.body.TopicBody
import com.maspam.etrain.training.data.dto.body.TrainingBody
import com.maspam.etrain.training.data.dto.body.UpdateSectionBody
import com.maspam.etrain.training.data.mapper.toSectionModel
import com.maspam.etrain.training.data.mapper.toTopicModel
import com.maspam.etrain.training.data.mapper.toTrainingModel
import com.maspam.etrain.training.domain.datasource.network.TrainingDataSource
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TopicModel
import com.maspam.etrain.training.domain.model.TrainingModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class RemoteTrainingDataSource(
    private val httpClient: HttpClient,
    private val fileReader: FileReader
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
        val info = trainingBody.img?.let { fileReader.uriToFileInfo(it) }

        return safeCall <BaseDto<TrainingDto>>{
            httpClient.post(
                urlString = constructUrl("/training/")
            ) {
                bearerAuth(token = token)
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", trainingBody.name ?: "")
                        append("desc", trainingBody.desc ?: "")
                        append("type_train", trainingBody.typeTrain)
                        append("type_train_ac", trainingBody.typeTrainAc)
                        append("location", trainingBody.location)
                        append("dateline", trainingBody.dateline ?: 0L)
                        append("attend", trainingBody.attend ?: 0L)
                        append("link", trainingBody.link)
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
            response.data?.toTrainingModel() ?: TrainingModel()
        }
    }

    override suspend fun updateTraining(
        token: String,
        trainingId: Int,
        trainingBody: TrainingBody
    ): Result<TrainingModel, NetworkError> {
        var info : FileInfo? = null
        if (trainingBody.img.toString().isNotEmpty()) {
            info = trainingBody.img.let { fileReader.uriToFileInfo(it ?: Uri.parse("")) }
        }
        return safeCall <BaseDto<TrainingDto>>{
            httpClient.patch(
                urlString = constructUrl("/training/$trainingId")
            ) {
                bearerAuth(token = token)
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", trainingBody.name ?: "")
                        append("desc", trainingBody.desc ?: "")
                        append("type_train", trainingBody.typeTrain)
                        append("type_train_ac", trainingBody.typeTrainAc)
                        append("location", trainingBody.location)
                        append("dateline", trainingBody.dateline ?: 0L)
                        append("attend", trainingBody.attend ?: 0L)
                        append("link", trainingBody.link)
                        if (trainingBody.img.toString().isNotEmpty()) {
                            info?.let {
                                append("img", info.bytes, Headers.build {
                                    append(HttpHeaders.ContentType, info.mimeType)
                                    append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                                })
                            }
                        }
                    }
                ))
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

    override suspend fun updateSection(
        token: String,
        sectionId: Int,
        jp: Int,
        sectionName: String
    ): Result<SectionModel, NetworkError> {
        val body = UpdateSectionBody(
            jp = jp,
            name = sectionName
        )
        return safeCall <BaseDto<SectionDto>>{
            httpClient.patch(
                urlString = constructUrl("/section/$sectionId")
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toSectionModel() ?: SectionModel()
        }
    }

    override suspend fun getAllTopic(token: String): Result<List<TopicModel>, NetworkError> {
        return safeCall<BaseDto<List<TopicDto>>> {
            httpClient.get(
                urlString = constructUrl("/topic/")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map { it.toTopicModel() } ?: emptyList()
        }
    }

    override suspend fun getTopicByID(
        token: String,
        topicId: Int
    ): Result<TopicModel, NetworkError> {
        return safeCall<BaseDto<TopicDto>> {
            httpClient.get(
                urlString = constructUrl("/topic/?id=$topicId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.toTopicModel() ?: TopicModel()
        }
    }

    override suspend fun deleteTopicById(
        token: String,
        topicId: Int
    ): Result<String, NetworkError> {
        return safeCall<BaseDto<String>> {
            httpClient.delete(
                urlString = constructUrl("/topic/$topicId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data ?: ""
        }
    }

    override suspend fun addTopic(
        token: String,
        name: String,
        sectionId: Int,
        content: String,
        image: Uri
    ): Result<String, NetworkError> {
        var info : FileInfo? = null
        if (image.toString().isNotEmpty()) {
            info = image.let { fileReader.uriToFileInfo(it ) }
        }

        return safeCall <BaseDto<TopicDto>>{
            httpClient.post(
                urlString = constructUrl("/topic/")
            ) {
                bearerAuth(token = token)
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", name)
                        append("section_id", sectionId)
                        append("content", content)
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
            response.message ?: ""
        }
    }

    override suspend fun updateTopic(
        token: String,
        topicBody: TopicBody
    ): Result<TopicModel, NetworkError> {

        var info : FileInfo? = null
        if (topicBody.img.toString().isNotEmpty()) {
            info = topicBody.img.let { fileReader.uriToFileInfo(it ?: Uri.parse("") ) }
        }

        return safeCall <BaseDto<TopicDto>>{
            httpClient.patch(
                urlString = constructUrl("/topic/${topicBody.topicId}")
            ) {
                bearerAuth(token = token)
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", topicBody.name ?: "")
                        append("content", topicBody.content ?: "")
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
            response.data?.toTopicModel() ?: TopicModel()
        }
    }


}