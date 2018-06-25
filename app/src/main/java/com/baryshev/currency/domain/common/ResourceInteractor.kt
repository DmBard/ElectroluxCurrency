package com.baryshev.currency.domain.common

import android.content.Context
import android.support.annotation.StringRes

interface IResourceInteractor {
    fun getString(@StringRes strRes: Int): String
}

class ResourceInteractor(private val context: Context) :
        IResourceInteractor {
    override fun getString(@StringRes strRes: Int): String = context.getString(strRes)
}