package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.domain.model.TrainingModel

interface TrainingDataSource {
    suspend fun getAllTraining(token: String): Result<List<TrainingModel>, NetworkError>
    suspend fun getOpenTraining(token:String, isPublish: Boolean): Result<List<TrainingModel>, NetworkError>
}