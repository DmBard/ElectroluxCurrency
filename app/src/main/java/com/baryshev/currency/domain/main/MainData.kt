package com.baryshev.currency.domain.main

const val UNDEFINED = -1.0

class MainData {

    data class Currency(
            var id: Long,
            var cc: String,
            var name: String)

    data class Exchange(
            var from: String = "",
            var to: String = "",
            var isCache: Boolean = true,
            var cacheDate: String = "",
            var error: String = "",
            var result: Double = UNDEFINED
            )
}
