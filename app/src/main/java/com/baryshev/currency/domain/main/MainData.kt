package com.baryshev.currency.domain.main

const val UNDEFINED = -1.0

class MainData {

    data class Currency(
            var cc: String,
            var name: String)

    data class Exchange(
            var from: Currency? = null,
            var to: Currency? = null,
            var isCache: Boolean = false,
            var cacheDate: String = "",
            var error: String = "",
            var result: Double = UNDEFINED
            )
}
