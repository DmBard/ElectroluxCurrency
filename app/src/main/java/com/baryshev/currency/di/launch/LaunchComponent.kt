package com.baryshev.currency.di.launch

import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.di.vm.VMActivityModule
import com.baryshev.currency.presentation.launch.view.LaunchActivity
import dagger.Subcomponent

@Subcomponent(modules = [VMActivityModule::class])
@PerActivity
interface LaunchComponent {

    fun inject(activity: LaunchActivity)
}