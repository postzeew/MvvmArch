package com.postzeew.mvvmarch.errors

interface ErrorInfoResolver {
    fun resolveErrorInfo(throwable: Throwable): ErrorInfo
}