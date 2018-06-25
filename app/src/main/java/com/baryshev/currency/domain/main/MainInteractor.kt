package com.baryshev.currency.domain.main

import com.baryshev.currency.R
import com.baryshev.currency.common.AppDb
import com.baryshev.currency.data.exchange.db.ExchangeEntity
import com.baryshev.currency.data.exchange.network.pojo.CurrencyExchangeResponse
import com.baryshev.currency.data.exchange.network.repo.ExchangeRepo
import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.common.exception.ExchangeException
import com.baryshev.currency.utils.extensions.formatToDateString
import io.reactivex.Single
import javax.inject.Inject

@PerActivity
class MainInteractor @Inject constructor(appDb: AppDb,
                                         private val exchangeRepo: ExchangeRepo,
                                         private val resourceInteractor: IResourceInteractor) {
    private val exchangeDao = appDb.exchangeDao()
    private val data = MainData.Exchange()
    private var exchangeKoeff: Double = UNDEFINED

    fun convert(from: String,
                to: String,
                ammount: Double,
                withUpdating: Boolean = true): Single<MainData.Exchange> {

        return if (!withUpdating && data.from == from && data.to == to && exchangeKoeff != UNDEFINED) {
            convertWithoutUpdating(ammount)
        } else {
            updateAndConvert(from, to, ammount)
        }
    }

    private fun updateAndConvert(from: String,
                                 to: String,
                                 ammount: Double): Single<MainData.Exchange> {
        data.from = from
        data.to = to
        return exchangeRepo.getExchange(from, to)
            .flatMap { response: CurrencyExchangeResponse ->
                if (response.success == true) {
                    val koeff = response.rates?.values?.get(0)?.value ?: UNDEFINED
                    exchangeKoeff = koeff
                    if (exchangeKoeff == UNDEFINED) {
                        data.cacheDate = ""
                        data.error = resourceInteractor.getString(R.string.error_common)
                        Single.error(ExchangeException(data.error))
                    } else {
                        data.error = ""
                        data.cacheDate = response.timestamp.formatToDateString()
                        data.result = ammount * koeff
                        Single.just(data)
                    }
                } else {
                    data.cacheDate = ""
                    data.error = response.error?.info ?: resourceInteractor.getString(R.string.error_common)
                    Single.error(ExchangeException(data.error))
                }
            }
            .doOnSuccess {
                exchangeDao.insertExchange(ExchangeEntity(from = data.from,
                                                          to = data.to,
                                                          value = exchangeKoeff,
                                                          date = data.cacheDate))
            }
            .onErrorReturnItem(run {
                val exchangeEntity = exchangeDao.findExchangeByCurrencies(data.from, data.to)
                exchangeEntity?.let {
                    data.cacheDate = it.date
                    data.isCache = true
                    data.result = ammount * it.value
                    data
                } ?: throw ExchangeException(data.error)
            })
    }

    private fun convertWithoutUpdating(ammount: Double): Single<MainData.Exchange> {
        return Single.fromCallable {
            data.apply {
                isCache = false
                error = ""
                cacheDate = ""
                result = ammount * exchangeKoeff
            }
        }
    }
}
