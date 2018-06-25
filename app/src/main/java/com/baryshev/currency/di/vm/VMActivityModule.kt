package com.baryshev.currency.di.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.baryshev.currency.di.common.PerActivity
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.launch.LaunchInteractor
import com.baryshev.currency.domain.main.MainInteractor
import com.baryshev.currency.presentation.common.ViewModelFactory
import com.baryshev.currency.presentation.launch.vm.LaunchVM
import com.baryshev.currency.presentation.main.vm.MainVM
import com.baryshev.currency.utils.rx.IRxScheduler
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
class VMActivityModule {

    @Provides
    @PerActivity
    fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
                               ): ViewModelProvider.Factory = ViewModelFactory(creators)

    @Provides
    @IntoMap
    @ViewModelKey(LaunchVM::class)
    @PerActivity
    fun provideLaunchVM(
            rxScheduler: IRxScheduler,
            launchInteractor: LaunchInteractor,
            resourceInteractor: IResourceInteractor): ViewModel {
        return LaunchVM(rxScheduler, launchInteractor, resourceInteractor)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MainVM::class)
    @PerActivity
    fun provideMainVM(
            rxScheduler: IRxScheduler,
            mainInteractor: MainInteractor,
            resourceInteractor: IResourceInteractor): ViewModel {
        return MainVM(rxScheduler, mainInteractor, resourceInteractor)
    }
}