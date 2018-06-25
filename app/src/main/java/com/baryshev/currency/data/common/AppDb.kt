package com.baryshev.currency.data.common

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.baryshev.currency.data.currency.db.CurrencyDao
import com.baryshev.currency.data.currency.db.CurrencyEntity
import com.baryshev.currency.data.exchange.db.ExchangeDao
import com.baryshev.currency.data.exchange.db.ExchangeEntity

@Database(entities = [ExchangeEntity::class, CurrencyEntity::class],
          version = 1,
          exportSchema = false)
abstract class AppDb : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao
    abstract fun currencyDao(): CurrencyDao
}