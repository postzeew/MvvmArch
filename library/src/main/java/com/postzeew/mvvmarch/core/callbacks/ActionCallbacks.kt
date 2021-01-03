package com.postzeew.mvvmarch.core.callbacks

class ActionCallbacks<T>(
    override val onActionStarted: (() -> Unit)? = null,
    override val onActionFailed: ((Throwable) -> Unit)? = null,
    val onActionFinished: ((T) -> Unit),
) : ActionCommonCallbacks(onActionStarted, onActionFailed)