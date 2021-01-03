package com.postzeew.mvvmarch.errors

data class ErrorInfo(
    val title: String,
    val description: String
) {
    override fun toString(): String {
        return "$title $description"
    }
}