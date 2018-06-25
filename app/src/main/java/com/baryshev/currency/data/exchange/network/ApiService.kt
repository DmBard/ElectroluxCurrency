package com.baryshev.currency.data.exchange.network

import com.baryshev.currency.data.exchange.network.pojo.Rates
import com.baryshev.currency.data.exchange.network.pojo.RatesTypeAdapter
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {

    companion object {
        private const val BASE_URL = "http://data.fixer.io/api/"
        private const val TIMEOUT: Long = 25
        private const val KEY_PARAM = "access_key"
        private const val KEY_VALUE = "225da0e180e3907e3c0872c765e41085"
    }

    fun init(): ExchangeApi = initRetrofit().create(ExchangeApi::class.java)

    private fun initRetrofit(): Retrofit {
        val okOkHttpBuilder = OkHttpClient.Builder()
        val okHttpClient = okOkHttpBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor { chain -> addApiKeyQueryParam(chain) }
            .build()

        val gson = GsonBuilder().registerTypeAdapter(Rates::class.java, RatesTypeAdapter()).create()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun addApiKeyQueryParam(chain: Interceptor.Chain): Response? {
        var request = chain.request()
        val url = request.url()
            .newBuilder()
            .addQueryParameter(KEY_PARAM,
                               KEY_VALUE)
            .build()
        request = request.newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }

}