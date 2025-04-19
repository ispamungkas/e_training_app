package com.maspam.etrain.training.data.remote_datasource.local.proto

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserSessionSerializer: Serializer<UserSession>{
    override val defaultValue: UserSession
        get() = UserSession()

    override suspend fun readFrom(input: InputStream): UserSession {
        return try {
            Json.decodeFromString(
                deserializer = UserSession.serializer(),
                string = input.readBytes().decodeToString()
            )

        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSession, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserSession.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}