package com.postzeew.mvvmarch.core.data

sealed class ViewCommand {

    data class ShowLoading(
        val type: Type
    ) : ViewCommand()

    data class HideLoading(
        val type: Type
    ) : ViewCommand()

    data class ShowError(
        val throwable: Throwable
    ) : ViewCommand()

    object HideError : ViewCommand()

    enum class Type {
        ON_SCREEN, OVER_SCREEN
    }
}