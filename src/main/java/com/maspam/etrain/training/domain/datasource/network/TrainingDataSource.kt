package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.data.dto.body.TrainingBody
import com.maspam.etrain.training.domain.model.SectionModel
import com.maspam.etrain.training.domain.model.TrainingModel

interface TrainingDataSource {
    suspend fun getAllTraining(token: String): Result<List<TrainingModel>, NetworkError>
    suspend fun getOpenTraining(token:String): Result<List<TrainingModel>, NetworkError>
    suspend fun getTrainingById(token: String, trainingId: Int): Result<TrainingModel, NetworkError>
    suspend fun addTraining(token: String, trainingBody: TrainingBody): Result<TrainingModel, NetworkError>
    suspend fun setPublishedTraining(token: String, trainingId: Int, isPublish: Boolean): Result<TrainingModel, NetworkError>
    suspend fun getAllSection(token: String): Result<List<SectionModel>, NetworkError>
    suspend fun getSectionByID(token: String, sectionId: Int): Result<SectionModel, NetworkError>
    suspend fun deleteSectionById(token: String,sectionId: Int): Result<String, NetworkError>
    suspend fun addSection(token: String, trainingId: Int, jp: Int, sectionName: String): Result<SectionModel, NetworkError>
}