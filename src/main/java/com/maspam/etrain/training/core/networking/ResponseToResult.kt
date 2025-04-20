package com.maspam.etrain.training.core.networking

import com.maspam.etrain.training.core.domain.utils.NetworkError
import com.maspam.etrain.training.core.domain.utils.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError>{
    return when(response.status.value){
        in 200..299 -> {
            try {
                Result.Success(data = response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(error = NetworkError.SERIALIZATION)
            }
        }
        401 -> Result.Error(error = NetworkError.UNAUTHORIZED)
        400 -> Result.Error(error = NetworkError.BAD_REQUEST)
        406 -> Result.Error(error = NetworkError.NOT_ACCEPTED)
        404 -> Result.Error(error = NetworkError.NOT_FOUND)
        408 -> Result.Error(error = NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(error = NetworkError.TOO_MANY_REQUEST)
        else -> Result.Error(error = NetworkError.SERVER_ERROR)
    }
}