package com.baryshev.currency.utils

import android.util.Log
import com.baryshev.currency.BuildConfig

inline fun debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

fun loge(tag: String? = "ERROR!", body: String, error: Throwable) {
    debug { Log.e(tag, body, error) }
}

fun logd(body: String, tag: String? = "DEBUG WEATHER!") {
    debug { Log.d(tag, body) }
}