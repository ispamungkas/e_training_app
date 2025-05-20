package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.domain.model.UserModel
import com.maspam.etrain.training.presentation.profile.state.UpdateDataProfileState

interface AuthenticationDataSource {
    suspend fun login(nip: String, password: String): Result<UserModel, NetworkError>
    suspend fun register(nip: String, name: String, isHead: Boolean): Result<UserModel, NetworkError>
    suspend fun generateOtp(nip: String): Result<String, NetworkError>
    suspend fun verifyOtp(otp: String): Result<String, NetworkError>
    suspend fun changePassword(nip: String, newPassword: String): Result<UserModel, NetworkError>
    suspend fun changeDataProfile(token: String, data: UpdateDataProfileState, id: Int): Result<UserModel, NetworkError>
    suspend fun getUser(id: Int): Result<UserModel, NetworkError>
    suspend fun getAllUser(): Result<List<UserModel>, NetworkError>
}