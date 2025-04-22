package com.maspam.etrain.training.domain.datasource.network

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.domain.model.KaryaNyataModel

interface KaryaNyataDataSource {
    suspend fun uploadKaryaNyata(token: String, att: Uri, enrollId: Int, userId: Int): Result<KaryaNyataModel, NetworkError>
    suspend fun updateStatusUploadKaryaNyata(token: String, karyaNyataId: Int, enrollId: Int, status: String): Result<KaryaNyataModel, NetworkError>
    suspend fun updateAttKaryaNyata(token: String, karyaNyataId: Int, att: Uri): Result<KaryaNyataModel, NetworkError>
}