package com.baryshev.currency.domain.main

import com.baryshev.currency.R
import com.baryshev.currency.data.common.AppDb
import com.baryshev.currency.data.exchange.db.ExchangeEntity
import com.baryshev.currency.data.exchange.network.pojo.ExchangeResponse
import com.baryshev.currency.data.exchange.network.repo.ExchangeRepo
import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.common.exception.ExchangeException
import com.baryshev.currency.utils.extensions.formatToDateString
import com.baryshev.currency.utils.extensions.round3
import io.reactivex.Single
import java.net.UnknownHostException
import javax.inject.Inject

@PerActivity
class MainInteractor @Inject constructor(appDb: AppDb,
                                         private val exchangeRepo: ExchangeRepo,
                                         private val resourceInteractor: IResourceInteractor) {
    private val exchangeDao = appDb.exchangeDao()
    private val currencyDao = appDb.currencyDao()
    private var exchangeKoeff: Double = UNDEFINED
    val data = MainData.Exchange()

    fun convert(from: String,
                to: String,
                amount: Double,
                needToRefresh: Boolean = true): Single<MainData.Exchange> {

        return if (!needToRefresh && exchangeKoeff != UNDEFINED) {
            convertWithoutUpdating(amount)
        } else {
            updateAndConvert(from, to, amount)
        }
    }

    private fun updateAndConvert(from: String,
                                 to: String,
                                 amount: Double): Single<MainData.Exchange> {
        return Single.fromCallable({
                                       val currencyFromEntity = currencyDao.findCurrencyByCc(from)
                                       val currencyToEntity = currencyDao.findCurrencyByCc(to)
                                       if (currencyFromEntity != null && currencyToEntity != null) {
                                           data.from = MainData.Currency(currencyFromEntity.cc,
                                                                         currencyFromEntity.name)
                                           data.to = MainData.Currency(currencyToEntity.cc,
                                                                       currencyToEntity.name)

                                           data
                                       } else {
                                           data.error = resourceInteractor.getString(R.string.error_common)
                                           throw ExchangeException(data.error)
                                       }
                                   })
            .flatMap { exchangeRepo.getExchange() }
            .doOnSuccess { if (it.success == true) saveExchangeDataInDb(it) }
            .flatMap { exchangeFromNetworkResponse(it, from, to, amount, data) }
            .onErrorResumeNext { throwable: Throwable ->
                Single.fromCallable {
                    exchangeFromDb(amount, handleError(throwable, data))
                }
            }
    }

    private fun handleError(throwable: Throwable,
                            data: MainData.Exchange) = data.apply {
        error = when (throwable) {
            is UnknownHostException -> resourceInteractor.getString(R.string.error_network)
            is ExchangeException -> throwable.message
                    ?: resourceInteractor.getString(R.string.error_common)
            else -> resourceInteractor.getString(R.string.error_common)
        }
    }

    private fun exchangeFromDb(amount: Double,
                               exchangeData: MainData.Exchange): MainData.Exchange {
        val fromEntity = exchangeDao.findExchangeByName(exchangeData.from?.cc ?: "")
        val toEntity = exchangeDao.findExchangeByName(exchangeData.to?.cc ?: "")

        return if (fromEntity?.date.equals(toEntity?.date)) {
            val koeff = calcExchangeKoeff(fromEntity?.rate, toEntity?.rate)
            if (koeff != UNDEFINED) {
                exchangeData.apply {
                    cacheDate = fromEntity?.date.orEmpty()
                    isCache = true
                    result = (amount * koeff).round3()
                }
            } else throw throw ExchangeException(exchangeData.error)
        } else throw throw ExchangeException(exchangeData.error)
    }

    private fun exchangeFromNetworkResponse(response: ExchangeResponse,
                                            from: String,
                                            to: String,
                                            amount: Double,
                                            data: MainData.Exchange): Single<MainData.Exchange>? {
        return if (response.success == true) {
            exchangeKoeff = calcExchangeKoeff(response.rates?.values?.get(from),
                                              response.rates?.values?.get(to))
            if (exchangeKoeff == UNDEFINED) {
                data.cacheDate = ""
                data.error = resourceInteractor.getString(R.string.error_common)
                Single.error(ExchangeException(data.error))
            } else {
                data.error = ""
                data.isCache = false
                data.cacheDate = ""
                data.result = (amount * exchangeKoeff).round3()
                Single.just(data)
            }
        } else {
            data.isCache = false
            data.cacheDate = ""
            data.error = response.error?.info ?: resourceInteractor.getString(R.string.error_common)
            Single.error(ExchangeException(data.error))
        }
    }

    private fun calcExchangeKoeff(fromRate: Double?, toRate: Double?): Double {
        val rateFrom = fromRate ?: UNDEFINED
        val rateTo = toRate ?: UNDEFINED
        return if (rateFrom != UNDEFINED && rateTo != UNDEFINED) (rateTo / rateFrom) else UNDEFINED
    }

    private fun saveExchangeDataInDb(response: ExchangeResponse) {
        response.rates?.values?.let {
            val exchangeEntities: MutableList<ExchangeEntity> = ArrayList()
            it.forEach {
                exchangeEntities.add(ExchangeEntity(it.key,
                                                    it.value,
                                                    response.timestamp.formatToDateString()))
            }
            exchangeDao.insertExchange(exchangeEntities)
        }
    }

    private fun convertWithoutUpdating(amount: Double): Single<MainData.Exchange> {
        return Single.fromCallable {
            data.apply {
                isCache = false
                error = ""
                cacheDate = ""
                result = (amount * exchangeKoeff).round3()
            }
        }
    }
}
