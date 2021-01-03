package com.postzeew.mvvmarch.core.presentation

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.postzeew.mvvmarch.R
import com.postzeew.mvvmarch.core.MvvmArch
import com.postzeew.mvvmarch.core.data.ViewCommand.*
import com.postzeew.mvvmarch.extensions.gone
import com.postzeew.mvvmarch.extensions.show
import com.postzeew.mvvmarch.views.OverScreenLoaderView
import com.postzeew.mvvmarch.views.ScreenStateView

abstract class ViewModelActivity<T : BaseViewModel>(viewModelImplClass: Class<out BaseViewModelImpl>) : AppCompatActivity() {
    protected val viewModel: T by lazy {
        @Suppress("UNCHECKED_CAST")
        ViewModelProvider(this, MvvmArch.viewModelFactory).get(viewModelImplClass) as T
    }

    private val screenStateView: ScreenStateView? by lazy {
        findViewById<ScreenStateView>(R.id.screenStateView)?.also { screenStateView ->
            screenStateView.onRetryClickListener = viewModel::onRetryClicked
        }
    }

    private val overScreenLoaderView: OverScreenLoaderView? by lazy {
        findViewById(R.id.overScreenLoaderView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToViewModel()
    }

    @CallSuper
    protected open fun subscribeToViewModel() {
        viewModel.overScreenError.observe(this, Observer { throwable ->
            val errorInfo = MvvmArch.errorInfoResolver.resolveErrorInfo(throwable)
            MvvmArch.messageProcessor.showMessage(errorInfo.toString())
        })

        viewModel.viewCommands.observe(this, Observer { viewCommand ->
            when (viewCommand) {
                is ShowLoading -> when (viewCommand.type) {
                    Type.ON_SCREEN -> screenStateView?.showLoading()
                    Type.OVER_SCREEN -> overScreenLoaderView?.show()
                }
                is HideLoading -> when (viewCommand.type) {
                    Type.ON_SCREEN -> screenStateView?.hideLoading()
                    Type.OVER_SCREEN -> overScreenLoaderView?.gone()
                }
                is ShowError -> {
                    val errorInfo = MvvmArch.errorInfoResolver.resolveErrorInfo(viewCommand.throwable)
                    screenStateView?.showError(errorInfo)
                }
                HideError -> screenStateView?.hideError()
            }
        })
    }
}