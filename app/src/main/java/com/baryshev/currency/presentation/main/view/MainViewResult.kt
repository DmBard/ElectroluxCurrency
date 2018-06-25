package com.baryshev.currency.presentation.main.view

import com.baryshev.currency.domain.main.MainData

sealed class MainViewResult {
    data class Success(val data: MainData.Exchange) : MainViewResult()
    data class Error(val message: String, val error: Throwable? = null) : MainViewResult()
    object Progress : MainViewResult()
}
