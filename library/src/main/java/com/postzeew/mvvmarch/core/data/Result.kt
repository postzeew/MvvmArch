package com.postzeew.mvvmarch.core.data

internal sealed class Result {

    companion object {
        fun <T> success(value: T) = Success(value)
        fun failure(throwable: Throwable) = Failure(throwable)
    }

    data class Success<T>(
        val value: T
    ) : Result()

    data class Failure(
        val throwable: Throwable
    ) : Result()
}