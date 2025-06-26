package com.maspam.etrain.training.domain.datasource.network

import android.net.Uri
import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.domain.model.KaryaNyataModel

interface KaryaNyataDataSource {
    suspend fun getAllKaryanyata(token: String): Result<List<KaryaNyataModel>, NetworkError>
    suspend fun getKaryaNyataById(token: String, karyaNyataId: Int): Result<KaryaNyataModel, NetworkError>
    suspend fun uploadKaryaNyata(token: String, att: Uri, enrollId: Int, userId: Int): Result<KaryaNyataModel, NetworkError>
    suspend fun updateStatusUploadKaryaNyata(token: String, karyaNyataId: Int, enrollId: Int, status: String, grade: String?): Result<KaryaNyataModel, NetworkError>
    suspend fun updateAttKaryaNyata(token: String, karyaNyataId: Int, att: Uri): Result<KaryaNyataModel, NetworkError>
}