package com.maspam.etrain.training.data.remote_datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.core.presentation.utils.FileReader
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.BaseDtoUser
import com.maspam.etrain.training.data.dto.UserDto
import com.maspam.etrain.training.data.dto.body.RegisterBody
import com.maspam.etrain.training.data.mapper.toUserModel
import com.maspam.etrain.training.domain.datasource.network.AuthenticationDataSource
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.profile.state.UpdateDataProfileState
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class RemoteAuthenticationDataSource(
    private val httpClient: HttpClient,
    private val fileReader: FileReader
): AuthenticationDataSource {

    override suspend fun login(nip: String, password: String): Result<UserModel, NetworkError> {
        val body = mapOf(
            "nip" to nip,
            "password" to password
        )

        return safeCall <BaseDtoUser<UserDto>>{
            httpClient.post(
                urlString = constructUrl(url = "/login/")
            ) {
                contentType(type = ContentType.Application.Json)
                setBody(body)
            }
        }.map { response ->
            println("coba cek $response")
            response.toUserModel()
        }
    }

    override suspend fun register(nip: String, name: String, role: String): Result<UserModel, NetworkError> {
        return safeCall <BaseDto<UserDto>>{
            httpClient.post(
                urlString = constructUrl(url = "/register/")
            ) {
                contentType(type = ContentType.Application.Json)
                setBody(RegisterBody(
                    nip = nip,
                    name = name,
                    role = role
                ))
            }
        }.map { response ->
            response.data?.toUserModel() ?: UserModel()
        }
    }

    override suspend fun generateOtp(nip: String): Result<String, NetworkError> {
        val body = mapOf(
            "nip" to nip
        )
        return safeCall <BaseDto<UserDto>>{
            httpClient.post(
                urlString = constructUrl(url = "/requestotp/")
            ) {
                contentType(type = ContentType.Application.Json)
                setBody(body)
            }
        }.map { response ->
            response.message ?: "unknown"
        }
    }

    override suspend fun verifyOtp(otp: String): Result<String, NetworkError> {
        val body = mapOf(
            "otp_code" to otp
        )
        return safeCall <BaseDto<UserDto>>{
            httpClient.post(
                urlString = constructUrl(url = "/validateotp/")
            ) {
                contentType(type = ContentType.Application.Json)
                setBody(body)
            }
        }.map { response ->
            response.message ?: "unknown"
        }
    }

    override suspend fun changePassword(
        nip: String,
        newPassword: String
    ): Result<UserModel, NetworkError> {
        val body = mapOf(
            "nip" to nip,
            "new_password" to newPassword
        )

        return safeCall <BaseDto<UserDto>>{
            httpClient.patch(
                urlString = constructUrl(url = "/updatepassword/")
            ) {
                contentType(type = ContentType.Application.Json)
                setBody(body)
            }
        }.map { response ->
            response.data?.toUserModel() ?: UserModel()
        }
    }

    override suspend fun changeDataProfile(
        token: String,
        data: UpdateDataProfileState,
        id: Int
    ): Result<UserModel, NetworkError> {
        val info = data.profileImage?.let { fileReader.uriToFileInfo(it) }

        return safeCall <BaseDto<UserDto>>{
            httpClient.patch(
                urlString = constructUrl("/updateuser/${id}")
            ) {
                bearerAuth(token = token)
                setBody(MultiPartFormDataContent(
                    formData {
                        append("name", data.name ?: "")
                        append("email", data.email ?: "")
                        append("p_number", data.phoneNumber ?: "")
                        append("address", data.address ?: "")
                        append("gender", data.gender ?: "")
                        append("c_school", data.currentSchool ?: "")
                        append("l_edu", data.lastStudy ?: "")
                        append("dob", data.dayOfBirth ?: 0)
                        info?.let {
                            append("img_profile", info.bytes, Headers.build {
                                append(HttpHeaders.ContentType, info.mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=${info.name}")
                            })
                        }
                    }
                ))
            }
        }.map { response ->
            response.data?.toUserModel() ?: UserModel()
        }
    }

    override suspend fun getUser(id: Int): Result<UserModel, NetworkError> {
        return safeCall <BaseDto<UserDto>>{
            httpClient.get(
                urlString = constructUrl("/login/?id=$id")
            )
        }.map { response ->
            response.data?.toUserModel() ?: UserModel()
        }
    }

    override suspend fun getAllUser(): Result<List<UserModel>, NetworkError> {
        return safeCall <BaseDto<List<UserDto>>>{
            httpClient.get(
                urlString = constructUrl("/login/")
            )
        }.map { response ->
            response.data?.map { it.toUserModel() } ?: emptyList()
        }
    }
}