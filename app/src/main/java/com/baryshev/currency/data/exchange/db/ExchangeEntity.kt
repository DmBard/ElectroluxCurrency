package com.baryshev.currency.data.exchange.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "exchanges")
data class ExchangeEntity(@PrimaryKey
                          var name: String,
                          var rate: Double,
                          var date: String)
