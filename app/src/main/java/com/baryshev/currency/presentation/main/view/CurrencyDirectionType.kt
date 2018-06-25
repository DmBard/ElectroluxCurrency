package com.baryshev.currency.presentation.main.view

import android.support.annotation.IntDef

@IntDef(UNDEFINED, FROM, TO)
@Retention(AnnotationRetention.SOURCE)
annotation class CurrencyDirectionType

const val UNDEFINED = 0
const val FROM = 1
const val TO = 2