package com.baryshev.currency.data.exchange.network.pojo


data class ExchangeResponse(val success: Boolean?,
                            val query: Query?,
                            val info: Info?,
                            val historical: String?,
                            val date: String?,
                            val result: Double?,
                            val error: Error?
                           )

data class Info(val timestamp: Long?,
                val rate: Double?
               )

data class Query(val from: String?,
                 val to: String?,
                 val amount: Int?
                )