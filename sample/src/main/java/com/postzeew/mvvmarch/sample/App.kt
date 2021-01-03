package com.postzeew.mvvmarch.sample

import android.app.Application
import com.postzeew.mvvmarch.core.MvvmArch
import com.postzeew.mvvmarch.sample.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MvvmArch.init(
            appContext = this,
            viewModelFactory = DaggerAppComponent.create().getAppViewModelFactory()
        )
    }
}