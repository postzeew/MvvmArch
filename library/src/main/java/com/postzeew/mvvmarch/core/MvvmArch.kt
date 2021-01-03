package com.postzeew.mvvmarch.core

import android.content.Context
import com.postzeew.mvvmarch.core.presentation.ViewModelFactory
import com.postzeew.mvvmarch.errors.DefaultErrorInfoResolver
import com.postzeew.mvvmarch.errors.ErrorInfoResolver
import com.postzeew.mvvmarch.messages.MessageProcessor
import com.postzeew.mvvmarch.messages.ToastMessageProcessor
import com.postzeew.mvvmarch.views.OverScreenLoaderView
import com.postzeew.mvvmarch.views.ScreenStateView

object MvvmArch {
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var messageProcessor: MessageProcessor
    lateinit var errorInfoResolver: ErrorInfoResolver
    lateinit var overScreenLoaderViewConfig: OverScreenLoaderView.Config
    lateinit var screenStateViewConfig: ScreenStateView.Config

    fun init(
        appContext: Context,
        viewModelFactory: ViewModelFactory,
        messageProcessor: MessageProcessor = ToastMessageProcessor(appContext),
        errorInfoResolver: ErrorInfoResolver = DefaultErrorInfoResolver(appContext),
        overScreenLoaderViewConfig: OverScreenLoaderView.Config = OverScreenLoaderView.Config(),
        screenStateViewConfig: ScreenStateView.Config = ScreenStateView.Config()
    ) {
        MvvmArch.viewModelFactory = viewModelFactory
        MvvmArch.messageProcessor = messageProcessor
        MvvmArch.errorInfoResolver = errorInfoResolver
        MvvmArch.overScreenLoaderViewConfig = overScreenLoaderViewConfig
        MvvmArch.screenStateViewConfig = screenStateViewConfig
    }
}