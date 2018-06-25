package com.baryshev.currency.presentation.main.view

import com.baryshev.currency.domain.main.MainData

sealed class CurrenciesViewResult {
    object Progress : CurrenciesViewResult()
    data class Success(val data: List<MainData.Currency>) : CurrenciesViewResult()
    data class Error(val message: String, val error: Throwable? = null) : CurrenciesViewResult()
}