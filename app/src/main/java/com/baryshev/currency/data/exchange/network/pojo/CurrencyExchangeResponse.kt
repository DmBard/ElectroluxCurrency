package com.baryshev.currency.data.exchange.network.pojo

data class CurrencyExchangeResponse(
    val success: Boolean?,
    val error: Error?,
    val timestamp: Long?,
    val base: String?,
    val date: String?,
    val rates: Rates?
)