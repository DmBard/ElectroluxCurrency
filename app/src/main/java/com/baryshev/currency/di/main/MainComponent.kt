package com.baryshev.currency.di.main

import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.di.common.PerFragment
import com.baryshev.currency.di.vm.VMActivityModule
import com.baryshev.currency.di.vm.VMFragmentModule
import com.baryshev.currency.presentation.main.view.CurrenciesDialogFragment
import com.baryshev.currency.presentation.main.view.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [VMActivityModule::class])
@PerActivity()
interface MainComponent {

    fun inject(target: MainActivity)
}