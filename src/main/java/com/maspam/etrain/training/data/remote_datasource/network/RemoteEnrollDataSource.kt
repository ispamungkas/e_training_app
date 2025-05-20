package com.maspam.etrain.training.data.remote_datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.CertificateDto
import com.maspam.etrain.training.data.dto.EnrollDto
import com.maspam.etrain.training.data.mapper.toCertificateModel
import com.maspam.etrain.training.data.mapper.toEnrollModel
import com.maspam.etrain.training.domain.datasource.network.EnrollDataSource
import com.maspam.etrain.training.domain.model.CertificateModel
import com.maspam.etrain.training.domain.model.EnrollModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteEnrollDataSource(
    private val httpClient: HttpClient,
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

    override suspend fun getDetailEnroll(
        token: String,
        enrollId: Int
    ): Result<EnrollModel, NetworkError> {
        return safeCall <BaseDto<EnrollDto>>{
            httpClient.get(
                urlString = constructUrl("/enroll/?id=$enrollId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.toEnrollModel() ?: EnrollModel()
        }
    }

    override suspend fun getEnrollByEnrollId(
        token: String,
        enrollId: Int
    ): Result<EnrollModel, NetworkError> {
        return safeCall <BaseDto<EnrollDto>>{
            httpClient.get(
                urlString = constructUrl("/enroll/?id=$enrollId")
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.toEnrollModel() ?: EnrollModel()
        }
    }

    override suspend fun updateAttendance(token: String, enrollId: Int): Result<EnrollModel, NetworkError> {
        val body = mapOf(
            "attandence" to true,
        )
        return safeCall <BaseDto<EnrollDto>>{
            httpClient.patch(
                urlString = constructUrl("/enroll/$enrollId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toEnrollModel() ?: EnrollModel()
        }
    }

    override suspend fun updateProgressTraining(
        token: String,
        enrollId: Int,
        section: Int,
        topic: Int
    ): Result<EnrollModel, NetworkError> {
        val body = mapOf(
            "p_learn" to section,
            "s_learn" to topic
        )
        return safeCall <BaseDto<EnrollDto>>{
            httpClient.patch(
                urlString = constructUrl("/enroll/$enrollId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toEnrollModel() ?: EnrollModel()
        }
    }

    override suspend fun takePostTest(
        token: String,
        enrollId: Int
    ): Result<EnrollModel, NetworkError> {
        val body = mapOf(
            "t_post" to true,
        )
        return safeCall <BaseDto<EnrollDto>>{
            httpClient.patch(
                urlString = constructUrl("/enroll/$enrollId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toEnrollModel() ?: EnrollModel()
        }
    }

    override suspend fun createCertificate(token: String, userId: Int, enrollId: Int): Result<CertificateModel, NetworkError> {
        val body = mapOf(
            "user" to userId,
            "enroll" to enrollId
        )
        return safeCall <BaseDto<CertificateDto>>{
            httpClient.post(
                urlString = constructUrl("/certificate/"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toCertificateModel() ?: CertificateModel()
        }
    }

    override suspend fun checkCertificate(
        token: String,
        value: String
    ): Result<String, NetworkError> {
        val body = mapOf(
            "check" to value,
        )
        return safeCall <BaseDto<String>>{
            httpClient.post(
                urlString = constructUrl("/certificate/verification"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.message ?: ""
        }
    }
}