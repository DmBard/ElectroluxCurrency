package com.baryshev.currency

import android.app.Application
import android.content.Context
import com.baryshev.currency.di.common.Injector

class App : Application() {

    var injector : Injector? = null
        private set

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun context() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        injector = Injector.getInstance(this)
    }
}

val Context.app: App get() = applicationContext as App