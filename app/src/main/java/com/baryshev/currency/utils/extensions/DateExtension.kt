package com.baryshev.currency.utils.extensions

import java.text.SimpleDateFormat
import java.util.*


const val DEFAULT_DATE_PATERN = "dd/MM/yyyy HH:mm"

fun Long?.formatToDateString(): String =
    this?.let { SimpleDateFormat(DEFAULT_DATE_PATERN, Locale.getDefault()).format(Date(this * 1000L)) } ?: ""