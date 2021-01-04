package com.postzeew.mvvmarch.core.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.postzeew.mvvmarch.R
import com.postzeew.mvvmarch.core.MvvmArch
import com.postzeew.mvvmarch.core.data.ViewCommand
import com.postzeew.mvvmarch.core.data.ViewCommand.Type
import com.postzeew.mvvmarch.extensions.gone
import com.postzeew.mvvmarch.extensions.show
import com.postzeew.mvvmarch.views.OverScreenLoaderView
import com.postzeew.mvvmarch.views.ScreenStateView

class ViewModelViewDelegate<T : BaseViewModel>(
    viewModelImplClass: Class<out BaseViewModelImpl>,
    rootView: View,
    viewModelStoreOwner: ViewModelStoreOwner,
    private val lifecycleOwner: LifecycleOwner
) {
    val viewModel: T by lazy {
        @Suppress("UNCHECKED_CAST")
        ViewModelProvider(viewModelStoreOwner, MvvmArch.viewModelFactory).get(viewModelImplClass) as T
    }

    val screenStateView: ScreenStateView? by lazy {
        rootView.findViewById<ScreenStateView>(R.id.screenStateView)?.also { screenStateView ->
            screenStateView.onRetryClickListener = viewModel::onRetryClicked
        }
    }

    val overScreenLoaderView: OverScreenLoaderView? by lazy {
        rootView.findViewById(R.id.overScreenLoaderView)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.onViewCreated()
        } else {
            viewModel.onViewRecreated()
        }
    }

    fun subscribeToViewModel() {
        viewModel.overScreenError.observe(lifecycleOwner, Observer { throwable ->
            val errorInfo = MvvmArch.errorInfoResolver.resolveErrorInfo(throwable)
            MvvmArch.messageProcessor.showMessage(errorInfo.toString())
        })

        viewModel.viewCommands.observe(lifecycleOwner, Observer { viewCommand ->
            when (viewCommand) {
                is ViewCommand.ShowLoading -> when (viewCommand.type) {
                    Type.ON_SCREEN -> screenStateView?.showLoading()
                    Type.OVER_SCREEN -> overScreenLoaderView?.show()
                }
                is ViewCommand.HideLoading -> when (viewCommand.type) {
                    Type.ON_SCREEN -> screenStateView?.hideLoading()
                    Type.OVER_SCREEN -> overScreenLoaderView?.gone()
                }
                is ViewCommand.ShowError -> {
                    val errorInfo = MvvmArch.errorInfoResolver.resolveErrorInfo(viewCommand.throwable)
                    screenStateView?.showError(errorInfo)
                }
                ViewCommand.HideError -> screenStateView?.hideError()
            }
        })
    }
}