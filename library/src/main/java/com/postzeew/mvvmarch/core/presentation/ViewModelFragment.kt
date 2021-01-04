package com.postzeew.mvvmarch.core.presentation

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class ViewModelFragment<T : BaseViewModel>(
    @LayoutRes contentLayoutId: Int,
    viewModelImplClass: Class<out BaseViewModelImpl>
) : Fragment(contentLayoutId) {
    private val viewModelViewDelegate by lazy {
        ViewModelViewDelegate<T>(
            viewModelImplClass = viewModelImplClass,
            rootView = requireNotNull(view),
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