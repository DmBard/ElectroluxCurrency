package com.baryshev.currency.data.exchange.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "exchanges")
data class ExchangeEntity(@PrimaryKey(autoGenerate = true)
                          var id: Long = 0,
                          var from: String,
                          var to: String,
                          var value: Double,
                          var date: String)
