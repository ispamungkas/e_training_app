package com.maspam.etrain.training.data.remote_datasource.network

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import com.maspam.etrain.training.core.domain.utils.map
import com.maspam.etrain.training.core.networking.constructUrl
import com.maspam.etrain.training.core.networking.safeCall
import com.maspam.etrain.training.data.dto.AnswerDto
import com.maspam.etrain.training.data.dto.BaseDto
import com.maspam.etrain.training.data.dto.PostTestDto
import com.maspam.etrain.training.data.dto.body.AnswerBody
import com.maspam.etrain.training.data.dto.body.PostTestBody
import com.maspam.etrain.training.data.mapper.toPostTestModel
import com.maspam.etrain.training.domain.datasource.network.PostTestDataSource
import com.maspam.etrain.training.domain.model.PostTestModel
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemotePostTestDataSource(
    private val httpClient: HttpClient
) : PostTestDataSource {
    override suspend fun getPostTest(token: String): Result<List<PostTestModel>, NetworkError> {
        return safeCall<BaseDto<List<PostTestDto>>> {
            httpClient.get(
                urlString = constructUrl("/posttest/"),
            ) {
                bearerAuth(token = token)
            }
        }.map { response ->
            response.data?.map {
                it.toPostTestModel()
            } ?: emptyList()
        }
    }

    override suspend fun createPostTest(
        token: String,
        trainingId: Int,
        sectionId: Int,
        question: List<String>
    ): Result<PostTestModel, NetworkError> {
        val body = PostTestBody(
            train = trainingId,
            section = sectionId,
            question = question
        )

        return safeCall<BaseDto<PostTestDto>> {
            httpClient.post(
                urlString = constructUrl("/posttest/"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toPostTestModel() ?: PostTestModel()
        }
    }

    override suspend fun updatePostTest(
        token: String,
        postTestId: Int,
        question: List<String>
    ): Result<PostTestModel, NetworkError> {
        val body = mapOf(
            "question" to question
        )

        return safeCall<BaseDto<PostTestDto>> {
            httpClient.patch(
                urlString = constructUrl("/posttest/$postTestId"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map { response ->
            response.data?.toPostTestModel() ?: PostTestModel()
        }
    }

    override suspend fun createAnswer(
        token: String,
        postTestId: Int,
        userId: Int,
        ans: List<String>
    ): Result<AnswerDto, NetworkError> {
        val body = AnswerBody(
            post = postTestId,
            user = userId,
            ans = ans
        )

        return safeCall<BaseDto<AnswerDto>> {
            httpClient.post(
                urlString = constructUrl("/answer/"),
            ) {
                bearerAuth(token = token)
                setBody(body)
            }
        }.map {
            it.data ?: AnswerDto()
        }
    }
}