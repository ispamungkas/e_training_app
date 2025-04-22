package com.maspam.etrain.training.data.remote_datasource.network

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.core.presentation.utils.FileReader
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.EnrollDto
import com.maspam.etrain.training.data.dto.KaryaNyataDto
import com.maspam.etrain.training.data.mapper.toKaryaNyataModel
import com.maspam.etrain.training.domain.datasource.network.KaryaNyataDataSource
import com.maspam.etrain.training.domain.model.KaryaNyataModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class RemoteKaryaNyataDataSource(
    private val httpClient: HttpClient,
    private val fileReader: FileReader,
): KaryaNyataDataSource {
    override suspend fun uploadKaryaNyata(token: String, att: Uri, enrollId: Int, userId: Int): Result<KaryaNyataModel, NetworkError> {
        val infoFile = att.let { fileReader.uriToFileInfo(it) }

        val body = mapOf(
            "t_karya_nyata" to true,
        )
        safeCall <BaseDto<EnrollDto>>{
            httpClient.patch(
                urlString = constructUrl("/enroll/$enrollId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }

        return safeCall <BaseDto<KaryaNyataDto>>{
            httpClient.post(
                urlString = constructUrl("/karyanyata/"),
            ) {
                bearerAuth(token = token)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("att", infoFile.bytes, Headers.build {
                                append(HttpHeaders.ContentType, infoFile.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${infoFile.name}.pdf")
                            })
                            append("enroll", enrollId)
                            append("user", userId)
                        }
                    )
                )
            }
        }.map { response ->
            response.data?.toKaryaNyataModel() ?: KaryaNyataModel()
        }
    }

    override suspend fun updateStatusUploadKaryaNyata(
        token: String,
        karyaNyataId: Int,
        enrollId: Int,
        status: String
    ): Result<KaryaNyataModel, NetworkError> {
        val body = mapOf(
            "status" to status,
        )

        val body2 = mapOf(
            "t_karya_nyata" to if (status == "accepted") {
                false
            } else {
                true
            }
        )
        safeCall <BaseDto<EnrollDto>>{
            httpClient.patch(
                urlString = constructUrl("/enroll/$enrollId"),
            ) {
                bearerAuth(token = token)
                setBody(body2)
            }
        }

        return safeCall <BaseDto<KaryaNyataDto>>{
            httpClient.patch(
                urlString = constructUrl("/karyanyata/$karyaNyataId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toKaryaNyataModel() ?: KaryaNyataModel()
        }
    }

    override suspend fun updateAttKaryaNyata(
        token: String,
        karyaNyataId: Int,
        att: Uri
    ): Result<KaryaNyataModel, NetworkError> {
        val infoFile = att.let { fileReader.uriToFileInfo(it) }

        return safeCall <BaseDto<KaryaNyataDto>>{
            httpClient.patch(
                urlString = constructUrl("/karyanyata/$karyaNyataId"),
            ) {
                bearerAuth(token = token)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("att", infoFile.bytes, Headers.build {
                                append(HttpHeaders.ContentType, infoFile.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${infoFile.name}")
                            })
                        }
                    )
                )
            }
        }.map { response ->
            response.data?.toKaryaNyataModel() ?: KaryaNyataModel()
        }
    }
}