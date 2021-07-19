package com.carlosblaya.theagilemonkeystest.data.network

import com.carlosblaya.theagilemonkeystest.util.Resource
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1),
    NoConnection(0)
}

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.error(getErrorMessage(e.code()), null)
            is SocketTimeoutException -> Resource.error(getErrorMessage(ErrorCodes.SocketTimeOut.code), null)
            is IOException -> Resource.error(getErrorMessage(ErrorCodes.NoConnection.code), null)

            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            ErrorCodes.NoConnection.code -> "No Connection"
            HttpURLConnection.HTTP_BAD_REQUEST -> "Bad request"
            HttpURLConnection.HTTP_UNAUTHORIZED -> "Unauthorized"
            HttpURLConnection.HTTP_FORBIDDEN -> "Forbidden"
            HttpURLConnection.HTTP_UNAVAILABLE -> "Service Unavailable"
            HttpURLConnection.HTTP_NOT_FOUND -> "Not Found"
            else -> "Something went wrong"
        }
    }
}