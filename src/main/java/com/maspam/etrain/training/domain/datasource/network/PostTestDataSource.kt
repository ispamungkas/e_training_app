package com.maspam.etrain.training.domain.datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.data.dto.AnswerDto
import com.maspam.etrain.training.domain.model.PostTestModel

interface PostTestDataSource {
    suspend fun getPostTest(token: String): Result<List<PostTestModel>, NetworkError>

    suspend fun createPostTest(
        token: String,
        trainingId: Int,
        sectionId: Int,
        question: List<String>
    ): Result<PostTestModel, NetworkError>

    suspend fun updatePostTest(
        token: String,
        postTestId: Int,
        question: List<String>
    ): Result<PostTestModel, NetworkError>

    suspend fun createAnswer(
        token: String,
        postTestId: Int,
        userId: Int,
        ans: List<String>
    ): Result<AnswerDto, NetworkError>
}