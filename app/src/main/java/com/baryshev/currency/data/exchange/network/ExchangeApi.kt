package com.baryshev.currency.data.exchange.network

import com.baryshev.currency.data.exchange.network.pojo.CurrencyExchangeResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {
    @GET("latest")
    fun getConvertedExchange(@Query("base") base: String = "USD"/*@Query("base") from: String*/): Single<CurrencyExchangeResponse>

}

