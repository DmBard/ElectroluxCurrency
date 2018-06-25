package com.baryshev.currency.presentation.common

sealed class SimpleViewResult {
    object Progress : SimpleViewResult()
    object Success : SimpleViewResult()
    data class Error(val message: String, val error: Throwable? = null) : SimpleViewResult()
}