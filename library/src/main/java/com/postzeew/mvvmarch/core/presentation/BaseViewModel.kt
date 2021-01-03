package com.postzeew.mvvmarch.core.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.postzeew.mvvmarch.core.callbacks.ActionCallbacks
import com.postzeew.mvvmarch.core.callbacks.ActionCommonCallbacks
import com.postzeew.mvvmarch.core.callbacks.EmptyActionCallbacks
import com.postzeew.mvvmarch.core.data.ActionType
import com.postzeew.mvvmarch.core.data.Result
import com.postzeew.mvvmarch.core.data.ViewCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BaseViewModel {
    val viewCommands: LiveData<ViewCommand>
    val overScreenError: LiveData<Throwable>

    suspend fun <T> executeOnScreenAction(action: suspend () -> T, resultLiveData: MutableLiveData<T>)
    suspend fun executeOnScreenAction(action: suspend () -> Unit)

    suspend fun <T> executeOverScreenAction(action: suspend () -> T, resultLiveData: MutableLiveData<T>)
    suspend fun executeOverScreenAction(action: suspend () -> Unit)

    suspend fun <T> executeCustomAction(action: suspend () -> T, callbacks: ActionCallbacks<T>)
    suspend fun executeCustomAction(action: suspend () -> Unit, callbacks: EmptyActionCallbacks)

    fun onRetryClicked()
}

abstract class BaseViewModelImpl : ViewModel(), BaseViewModel {
    override val viewCommands = MutableLiveData<ViewCommand>()
    override val overScreenError = SingleLiveEvent<Throwable>()

    override suspend fun <T> executeOnScreenAction(action: suspend () -> T, resultLiveData: MutableLiveData<T>) {
        executeActionInternal(action, ActionType.ON_SCREEN, resultLiveData)
    }

    override suspend fun executeOnScreenAction(action: suspend () -> Unit) {
        executeActionInternal(action, ActionType.ON_SCREEN)
    }

    override suspend fun <T> executeOverScreenAction(action: suspend () -> T, resultLiveData: MutableLiveData<T>) {
        executeActionInternal(action, ActionType.OVER_SCREEN, resultLiveData)
    }

    override suspend fun executeOverScreenAction(action: suspend () -> Unit) {
        executeActionInternal(action, ActionType.OVER_SCREEN)
    }

    override suspend fun <T> executeCustomAction(action: suspend () -> T, callbacks: ActionCallbacks<T>) {
        executeCustomActionInternal(action, callbacks) { result ->
            callbacks.onActionFinished.invoke(result)
        }
    }

    override suspend fun executeCustomAction(action: suspend () -> Unit, callbacks: EmptyActionCallbacks) {
        executeCustomActionInternal(action, callbacks) {
            callbacks.onActionFinished.invoke()
        }
    }

    @CallSuper
    override fun onRetryClicked() {
        viewCommands.value = ViewCommand.HideError
    }

    private suspend fun <T> executeActionInternal(
        action: suspend () -> T,
        actionType: ActionType,
        resultLiveData: MutableLiveData<T>? = null
    ) {
        showLoading(actionType)
        val result = executeActionOnBackgroundThread(action)
        hideLoading(actionType)
        when (result) {
            is Result.Success<*> -> processSuccessResult(result, resultLiveData)
            is Result.Failure -> processFailureResult(result, actionType)
        }
    }

    private fun showLoading(actionType: ActionType) {
        viewCommands.value = ViewCommand.ShowLoading(type = actionType.toLoadingType())
    }

    private fun hideLoading(actionType: ActionType) {
        viewCommands.value = ViewCommand.HideLoading(type = actionType.toLoadingType())
    }

    private fun <T> processSuccessResult(result: Result.Success<*>, resultLiveData: MutableLiveData<T>?) {
        resultLiveData?.let { liveData ->
            @Suppress("UNCHECKED_CAST")
            liveData.value = result.value as T
        }
    }

    private fun processFailureResult(result: Result.Failure, actionType: ActionType) {
        when (actionType) {
            ActionType.ON_SCREEN -> viewCommands.value = ViewCommand.ShowError(throwable = result.throwable)
            ActionType.OVER_SCREEN -> overScreenError.value = result.throwable
        }
    }

    private suspend fun <T> executeCustomActionInternal(
        action: suspend () -> T,
        callbacks: ActionCommonCallbacks,
        onActionFinished: (T) -> Unit
    ) {
        callbacks.onActionStarted?.invoke()
        val result = executeActionOnBackgroundThread(action)
        @Suppress("UNCHECKED_CAST")
        when (result) {
            is Result.Success<*> -> onActionFinished.invoke(result.value as T)
            is Result.Failure -> callbacks.onActionFailed?.invoke(result.throwable)
        }
    }

    private suspend fun <T> executeActionOnBackgroundThread(action: suspend () -> T): Result {
        return withContext(Dispatchers.IO) {
            com.postzeew.mvvmarch.extensions.runCatching {
                action.invoke()
            }
        }
    }

    private fun ActionType.toLoadingType(): ViewCommand.Type {
        return when (this) {
            ActionType.ON_SCREEN -> ViewCommand.Type.ON_SCREEN
            ActionType.OVER_SCREEN -> ViewCommand.Type.OVER_SCREEN
        }
    }
}