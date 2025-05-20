package com.maspam.etrain.training.domain.datasource.local.proto

import com.maspam.etrain.training.data.remote_datasource.local.proto.UserSession
import com.maspam.etrain.training.domain.model.UserModel

interface UserSessionDataSource {
    suspend fun update(data: UserModel)
    suspend fun updateProfile(data: UserModel)
    suspend fun delete()
    suspend fun getUserSession(): UserSession
    suspend fun getToken(): String
    suspend fun getNip(): String
    suspend fun getId(): Int
    suspend fun updateTakenTraining(idTraining: Int)
    suspend fun getTakeTraining(): List<Int>
}