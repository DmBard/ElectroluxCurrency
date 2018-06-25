package com.baryshev.currency.utils.extensions

import kotlin.math.round


inline fun String?.toDoubleOrDefault(code: () -> Double): Double =
    if (this.isNullOrEmpty()) code() else this?.toDoubleOrNull() ?: code()

fun String?.isDouble(): Boolean = this?.toDoubleOrNull() != null ?: false

fun Double.round3(): Double = round(this * 1000) / 1000