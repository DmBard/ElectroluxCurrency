package com.baryshev.currency.data.exchange.network

import com.baryshev.currency.data.exchange.network.pojo.ExchangeResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ExchangeApi {
    @GET("latest")
    fun getConvertedExchange(): Single<ExchangeResponse>
}

