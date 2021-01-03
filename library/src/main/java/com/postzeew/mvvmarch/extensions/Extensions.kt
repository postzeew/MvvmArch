package com.postzeew.mvvmarch.extensions

import com.postzeew.mvvmarch.core.data.Result

internal inline fun <R> runCatching(block: () -> R): Result {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}