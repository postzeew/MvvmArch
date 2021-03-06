package com.postzeew.mvvmarch.core.presentation

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

abstract class ViewModelActivity<T : BaseViewModel>(viewModelImplClass: Class<out BaseViewModelImpl>) : AppCompatActivity() {
    private val viewModelViewDelegate by lazy {
        ViewModelViewDelegate<T>(
            viewModelImplClass = viewModelImplClass,
            rootView = window.decorView.findViewById(android.R.id.content),
            viewModelStoreOwner = this,
            lifecycleOwner = this
        )
    }

    protected val viewModel by lazy { viewModelViewDelegate.viewModel }

    private val screenStateView by lazy { viewModelViewDelegate.screenStateView }
    private val overScreenLoaderView by lazy { viewModelViewDelegate.overScreenLoaderView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToViewModel()
        viewModelViewDelegate.onCreate(savedInstanceState)
    }

    @CallSuper
    protected open fun subscribeToViewModel() {
        viewModelViewDelegate.subscribeToViewModel()
    }
}