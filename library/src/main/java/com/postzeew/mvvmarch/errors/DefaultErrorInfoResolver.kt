package com.postzeew.mvvmarch.errors

import android.content.Context
import androidx.annotation.StringRes
import com.postzeew.mvvmarch.R
import retrofit2.HttpException

private const val JAVA_NET = "java.net"

private val CLIENT_ERROR_CODES = 400..499
private val SERVER_ERROR_CODES = 500..599

class DefaultErrorInfoResolver(
    private val appContext: Context
) : ErrorInfoResolver {

    override fun resolveErrorInfo(throwable: Throwable): ErrorInfo {
        throwable.printStackTrace()
        return when {
            throwable is HttpException -> processHttpException(throwable)
            throwable.javaClass.name.startsWith(JAVA_NET) -> ErrorInfo(
                getString(R.string.error_network_unavailable_title),
                getString(R.string.error_network_unavailable_description)
            )
            else -> ErrorInfo(
                getString(R.string.error_unknown_title),
                getString(R.string.error_unknown_description)
            )
        }
    }

    private fun processHttpException(exception: HttpException): ErrorInfo {
        return when (exception.code()) {
            in CLIENT_ERROR_CODES -> ErrorInfo(
                getString(R.string.error_client_title),
                getString(R.string.error_client_description)
            )
            in SERVER_ERROR_CODES -> ErrorInfo(
                getString(R.string.error_server_title),
                getString(R.string.error_server_description)
            )
            else -> ErrorInfo(
                getString(R.string.error_unknown_title),
                getString(R.string.error_unknown_description)
            )
        }
    }

    private fun getString(@StringRes stringResId: Int): String {
        return appContext.getString(stringResId)
    }
}