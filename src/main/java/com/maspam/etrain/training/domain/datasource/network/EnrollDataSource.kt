package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.domain.model.CertificateModel
import com.maspam.etrain.training.domain.model.EnrollModel

interface EnrollDataSource {
    suspend fun enrollTraining(token: String, trainingId: Int, userId: Int): Result<String, NetworkError>
    suspend fun getAllEnroll(token: String): Result<List<EnrollModel>, NetworkError>
    suspend fun getEnrollById(token: String, id: Int): Result<List<EnrollModel>, NetworkError>
    suspend fun updateProgressTraining(token: String,enrollId: Int, section: Int, topic: Int): Result<EnrollModel, NetworkError>
    suspend fun takePostTest(token: String, enrollId: Int): Result<EnrollModel, NetworkError>
    suspend fun createCertificate(token: String, userId: Int, enrollId: Int): Result<CertificateModel, NetworkError>
}