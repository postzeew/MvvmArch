package com.postzeew.mvvmarch.sample.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.postzeew.mvvmarch.core.presentation.BaseViewModel
import com.postzeew.mvvmarch.core.presentation.BaseViewModelImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

interface HomeViewModel : BaseViewModel {
    val time: LiveData<String>
    fun onGetTimeButtonClicked()
}

class HomeViewModelImpl @Inject constructor() : BaseViewModelImpl(), HomeViewModel {
    override val time = MutableLiveData<String>()

    override fun onGetTimeButtonClicked() {
        viewModelScope.launch {
            executeOverScreenAction(::getTime, time)
        }
    }

    override fun onRetryClicked() {
        super.onRetryClicked()
        viewModelScope.launch {
            executeOnScreenAction(::getTime, time)
        }
    }

    private suspend fun getTime(): String {
        delay(3000)
        return "12:00"
    }
}