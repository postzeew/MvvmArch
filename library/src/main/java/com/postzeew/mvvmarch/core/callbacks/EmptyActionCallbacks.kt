package com.postzeew.mvvmarch.core.callbacks

class EmptyActionCallbacks(
    override val onActionStarted: (() -> Unit)? = null,
    override val onActionFailed: ((Throwable) -> Unit)? = null,
    val onActionFinished: (() -> Unit)
) : ActionCommonCallbacks(onActionStarted, onActionFailed)