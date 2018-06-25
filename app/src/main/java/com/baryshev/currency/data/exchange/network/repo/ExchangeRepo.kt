package com.baryshev.currency.data.exchange.network.repo

import com.baryshev.currency.data.exchange.network.ExchangeApi
import com.baryshev.currency.data.exchange.network.pojo.ExchangeResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ExchangeRepo @Inject constructor(private val exchangeApi: ExchangeApi) {

    fun getExchange(): Single<ExchangeResponse> {
        return exchangeApi.getConvertedExchange()
    }
}