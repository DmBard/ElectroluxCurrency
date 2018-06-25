package com.baryshev.currency.di.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.baryshev.currency.di.common.PerFragment
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.main.CurrenciesInteractor
import com.baryshev.currency.presentation.common.ViewModelFactory
import com.baryshev.currency.presentation.main.vm.CurrenciesVM
import com.baryshev.currency.utils.rx.IRxScheduler
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
class VMFragmentModule {

    @Provides
    @PerFragment
    fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
                               ): ViewModelProvider.Factory = ViewModelFactory(creators)

    @Provides
    @IntoMap
    @ViewModelKey(CurrenciesVM::class)
    @PerFragment
    fun provideCurrenciesVM(rxScheduler: IRxScheduler,
                            currenciesInteractor: CurrenciesInteractor,
                            resourceInteractor: IResourceInteractor): ViewModel {
        return CurrenciesVM(rxScheduler, currenciesInteractor, resourceInteractor)
    }
}