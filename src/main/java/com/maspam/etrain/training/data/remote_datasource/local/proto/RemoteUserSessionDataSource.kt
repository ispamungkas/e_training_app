package com.maspam.etrain.training.data.remote_datasource.local.proto

import android.content.Context
import androidx.datastore.dataStore
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

val Context.datastore by dataStore("user-session.json", UserSessionSerializer)

class RemoteUserSessionDataSource(
    private val context: Context
): UserSessionDataSource {
    override suspend fun update(data: UserModel) {
        val role: String = if (data.nip == "0000") {
            "super"
        } else if (data.isHead == true) {
            "head"
        } else {
            "teacher"
        }

        context.datastore.updateData {
            it.copy(
                token = data.token,
                id = data.id,
                role = role,
                nip = data.nip,
                name = data.name
            )
        }
    }

    override suspend fun delete() {
        context.datastore.updateData {
            UserSession()
        }
    }

    override suspend fun getToken(): String {
        val token = context.datastore.data.first().token
        return token ?: ""
    }

    override suspend fun getNip(): String {
        val nip = context.datastore.data.first().nip
        return nip ?: ""
    }

    override suspend fun getUserSession(): Flow<UserSession> {
        return context.datastore.data
    }

    override suspend fun getId(): Int {
        return context.datastore.data.first().id ?: 0
    }

}