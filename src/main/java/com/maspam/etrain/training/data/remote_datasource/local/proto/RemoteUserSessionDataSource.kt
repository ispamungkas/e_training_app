package com.maspam.etrain.training.data.remote_datasource.local.proto

import android.content.Context
import androidx.datastore.dataStore
import com.maspam.etrain.training.domain.datasource.local.proto.UserSessionDataSource
import com.maspam.etrain.training.domain.model.UserModel
import kotlinx.coroutines.flow.first

val Context.datastore by dataStore("user-session.json", UserSessionSerializer)

class RemoteUserSessionDataSource(
    private val context: Context
): UserSessionDataSource {
    override suspend fun update(data: UserModel) {
        val role: String = if (data.role == "Super User") {
            "Super User"
        } else if (data.role == "Head School") {
            "Head School"
        } else if (data.role == "Kepala Bidang") {
            "Kepala Bidang"
        } else {
            "Teacher"
        }

        context.datastore.updateData {
            it.copy(
                token = data.token,
                id = data.id,
                role = role,
                nip = data.nip,
                name = data.name,
                image = data.imageProfile,
                enroll = data.enrolls?.map { data -> data.train ?: 0 } ?: emptyList()
            )
        }
    }

    override suspend fun updateProfile(data: UserModel) {
        context.datastore.updateData {
            it.copy(
                nip = data.nip,
                name = data.name,
                image = data.imageProfile,
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

    override suspend fun getUserSession(): UserSession {
        return context.datastore.data.first()
    }

    override suspend fun getId(): Int {
        return context.datastore.data.first().id ?: 0
    }

    override suspend fun updateTakenTraining(idTraining: Int) {
        context.datastore.updateData {
            val data = it.enroll.toMutableList()
            data.add(idTraining)
            it.copy(
                enroll = data
            )
        }
    }

    override suspend fun getTakeTraining(): List<Int> {
        return context.datastore.data.first().enroll
    }


}