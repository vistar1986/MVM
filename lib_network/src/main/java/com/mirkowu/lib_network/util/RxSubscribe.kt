package com.mirkowu.lib_network.util

import autodispose2.ObservableSubscribeProxy
import com.mirkowu.lib_network.AbsRxObserver
import com.mirkowu.lib_network.ErrorBean
import com.mirkowu.lib_network.ErrorType


fun <T> ObservableSubscribeProxy<T>.subscribe(
        onSuccess: (T?) -> Unit = {},
        onFailure: (ErrorBean) -> Unit = {}) {
    subscribe(object : AbsRxObserver<T>() {
        override fun onFailure(errorType: ErrorType, code: Int, msg: String) {
            onFailure(ErrorBean(errorType, code, msg))
        }

        override fun onSuccess(data: T) {
            onSuccess(data)
        }
    })
}