package com.baryshev.currency.di.common

import android.arch.persistence.room.Room
import android.content.Context
import com.baryshev.currency.data.common.AppDb
import com.baryshev.currency.data.exchange.network.ApiService
import com.baryshev.currency.data.exchange.network.ExchangeApi
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.common.ResourceInteractor
import com.baryshev.currency.utils.rx.IRxScheduler
import com.baryshev.currency.utils.rx.RxScheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = appContext

    @Singleton
    @Provides
    fun provideRxScheduler(): IRxScheduler = RxScheduler()

    @Singleton
    @Provides
    fun provideResourceInteractor(): IResourceInteractor = ResourceInteractor(
            appContext)

    @Singleton
    @Provides
    fun provideRetrofit(): ExchangeApi = ApiService().init()

    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDb = Room.databaseBuilder(context,
                                                                            AppDb::class.java,
                                                                            "exchange-db").build()


}