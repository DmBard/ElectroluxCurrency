package com.baryshev.currency.domain.launch

import android.content.Context
import com.baryshev.currency.common.AppDb
import com.baryshev.currency.data.currency.db.CurrencyEntity
import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.utils.logd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import javax.inject.Inject


const val CURRENCIES_JSON_FILE_NAME = "currencies.json"

@PerActivity
class LaunchInteractor @Inject constructor(val context: Context, appDb: AppDb) {
    private val currencyDao = appDb.currencyDao()

    fun addCurrenciesToDBIfNeeds(): Completable =
            Completable.fromAction {
                val count = currencyDao.count()
                logd(body = count.toString())
                if (count == 0) {
                    var reader: Reader? = null
                    try {
                        reader = BufferedReader(InputStreamReader(context.assets.open(CURRENCIES_JSON_FILE_NAME)))
                        val currencies: List<CurrencyEntity> = Gson().fromJson(reader,
                                                                               object : TypeToken<List<CurrencyEntity>>() {}.type)
                        currencyDao.insertCurrency(*currencies.toTypedArray())
                    } finally {
                        reader?.close()
                    }
                }
            }
}