package com.postzeew.mvvmarch.sample.di

import com.postzeew.mvvmarch.sample.presentation.AppViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class])
interface AppComponent {
    fun getAppViewModelFactory(): AppViewModelFactory
}