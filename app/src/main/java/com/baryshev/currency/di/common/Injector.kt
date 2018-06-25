package com.baryshev.currency.di.common

import android.content.Context
import com.baryshev.currency.di.launch.LaunchComponent
import com.baryshev.currency.di.main.CurrenciesComponent
import com.baryshev.currency.di.main.MainComponent
import com.baryshev.currency.di.vm.VMActivityModule
import com.baryshev.currency.di.vm.VMFragmentModule
import com.baryshev.currency.utils.SingletonHolder

class Injector private constructor(context: Context) {

    private val appComponent: AppComponent = DaggerAppComponent.builder().appModule(AppModule(context)).build()
    private var launchComponent: LaunchComponent? = null
    private var currenciesComponent: CurrenciesComponent? = null
    private var mainComponent: MainComponent? = null


    companion object : SingletonHolder<Injector, Context>(::Injector)

    fun initLaunchComponent(): LaunchComponent? = launchComponent
            ?: appComponent.plusLaunchComponent(VMActivityModule())

    fun clearLaunchComponent() {
        launchComponent = null
    }

    fun initCurrenciesComponent(): CurrenciesComponent? = currenciesComponent
            ?: appComponent.plusCurrenciesComponent(VMFragmentModule())

    fun clearCurrenciesComponent() {
        currenciesComponent = null
    }

    fun initMainComponent(): MainComponent? = mainComponent
            ?: appComponent.plusMainComponent(VMActivityModule())

    fun clearMainComponent() {
        currenciesComponent = null
    }
}