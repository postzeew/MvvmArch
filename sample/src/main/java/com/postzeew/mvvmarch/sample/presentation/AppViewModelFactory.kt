package com.postzeew.mvvmarch.sample.presentation

import androidx.lifecycle.ViewModel
import com.postzeew.mvvmarch.core.presentation.ViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AppViewModelFactory @Inject constructor(
    private val viewModelsProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelFactory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModelsProviders[modelClass]
            ?: throw IllegalArgumentException("ViewModelProvider for $modelClass not found.")
        @Suppress("UNCHECKED_CAST")
        return viewModelProvider.get() as T
    }
}