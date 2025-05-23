package com.maspam.etrain.training.core.presentation.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class FileReader(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uriToFileInfo(contentUri: Uri): FileInfo {
        return withContext(ioDispatcher) {
            val bytes = context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                } ?: byteArrayOf()

            val mimeType = context.contentResolver.getType(contentUri) ?: ""

            val extension = when (mimeType) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                "image/gif" -> ".gif"
                else -> ""
            }

            val fileName = UUID.randomUUID().toString() + extension

            FileInfo(
                name = fileName,
                mimeType = mimeType,
                bytes = bytes
            )
        }
    }
}

class FileInfo(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray
)