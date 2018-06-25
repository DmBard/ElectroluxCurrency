package com.baryshev.currency.presentation.main.vm

import android.arch.lifecycle.MutableLiveData
import com.baryshev.currency.R
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.main.CurrenciesInteractor
import com.baryshev.currency.presentation.common.BaseVM
import com.baryshev.currency.presentation.main.view.CurrenciesViewResult
import com.baryshev.currency.utils.rx.IRxScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrenciesVM(private val rxScheduler: IRxScheduler,
                   private val interactor: CurrenciesInteractor,
                   private val resourceInteractor: IResourceInteractor) : BaseVM() {

    internal val data: MutableLiveData<CurrenciesViewResult> = MutableLiveData()


    fun init() {
        data.value = CurrenciesViewResult.Progress
        val disposable = interactor.getCurrencies()
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
            .subscribe({
                           data.value = CurrenciesViewResult.Success(it)
                       }, { e: Throwable ->
                           run {
                               data.value = CurrenciesViewResult.Error(resourceInteractor.getString(
                                       R.string.error_common), e)
                           }
                       })
        addDisposable { disposable }
    }
}