package com.baryshev.currency.domain.main

import com.baryshev.currency.data.common.AppDb
import com.baryshev.currency.data.currency.db.CurrencyEntity
import com.baryshev.currency.di.common.PerFragment
import io.reactivex.Single
import javax.inject.Inject

@PerFragment
class CurrenciesInteractor @Inject constructor(appDb: AppDb) {
    private val currencyDao = appDb.currencyDao()

    fun getCurrencies(): Single<List<MainData.Currency>> =
            Single.fromCallable { currencyDao.getAllCurrencies() }
                .map { t: List<CurrencyEntity> ->
                    with(t) { map { MainData.Currency(it.cc, it.name) } }
                }
}