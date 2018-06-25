package com.baryshev.currency.utils.extensions


inline fun String?.orEmpty(code: () -> String): String = if (this.isNullOrEmpty()) code() else this ?: code()