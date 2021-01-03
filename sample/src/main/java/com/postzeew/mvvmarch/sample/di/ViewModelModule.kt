package com.postzeew.mvvmarch.sample.di

import androidx.lifecycle.ViewModel
import com.postzeew.mvvmarch.sample.presentation.HomeViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModelImpl::class)
    fun bindHomeViewModel(impl: HomeViewModelImpl): ViewModel
}