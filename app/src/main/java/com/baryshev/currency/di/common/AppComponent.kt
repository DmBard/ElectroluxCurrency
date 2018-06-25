package com.baryshev.currency.di.common

import android.content.Context
import com.baryshev.currency.di.launch.LaunchComponent
import com.baryshev.currency.di.main.CurrenciesComponent
import com.baryshev.currency.di.main.MainComponent
import com.baryshev.currency.di.vm.VMActivityModule
import com.baryshev.currency.di.vm.VMFragmentModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun context(): Context

    fun plusLaunchComponent(viewModel: VMActivityModule): LaunchComponent

    fun plusCurrenciesComponent(viewModel: VMFragmentModule): CurrenciesComponent

    fun plusMainComponent(viewModel: VMActivityModule): MainComponent
}