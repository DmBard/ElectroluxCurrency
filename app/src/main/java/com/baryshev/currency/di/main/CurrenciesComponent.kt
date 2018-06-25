package com.baryshev.currency.di.main

import com.baryshev.currency.di.common.PerFragment
import com.baryshev.currency.di.vm.VMFragmentModule
import com.baryshev.currency.presentation.main.view.CurrenciesDialogFragment
import dagger.Subcomponent

@Subcomponent(modules = [VMFragmentModule::class])
@PerFragment
interface CurrenciesComponent {

    fun inject(target: CurrenciesDialogFragment)
}