package com.postzeew.mvvmarch.messages

import android.content.Context
import android.widget.Toast

class ToastMessageProcessor(
    private val appContext: Context
) : MessageProcessor {

    override fun showMessage(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }
}