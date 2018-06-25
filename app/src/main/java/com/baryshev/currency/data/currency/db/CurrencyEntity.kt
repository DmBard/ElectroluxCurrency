package com.baryshev.currency.data.currency.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val CURRENCY_TABLE_NAME = "currencies"

@Entity(tableName = CURRENCY_TABLE_NAME)
data class CurrencyEntity(@PrimaryKey
                          var cc: String,
                          var symbol: String,
                          var name: String)
