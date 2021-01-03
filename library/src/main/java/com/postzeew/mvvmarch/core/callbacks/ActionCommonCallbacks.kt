package com.postzeew.mvvmarch.core.callbacks

open class ActionCommonCallbacks(
    open val onActionStarted: (() -> Unit)?,
    open val onActionFailed: ((Throwable) -> Unit)?
)