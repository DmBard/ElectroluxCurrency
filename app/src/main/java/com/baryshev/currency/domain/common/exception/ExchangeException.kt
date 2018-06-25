package com.baryshev.currency.domain.common.exception

import java.lang.Exception

class ExchangeException() : Exception() {
    constructor(info: String?) : this() {
        Exception(info)
    }
}