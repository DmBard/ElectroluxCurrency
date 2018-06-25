package com.baryshev.currency.presentation.main.vm

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.domain.main.MainInteractor
import com.baryshev.currency.presentation.common.BaseVM
import com.baryshev.currency.presentation.main.view.AMOUNT_DEFAULT
import com.baryshev.currency.presentation.main.view.FROM_DEFAULT
import com.baryshev.currency.presentation.main.view.MainViewResult
import com.baryshev.currency.presentation.main.view.TO_DEFAULT
import com.baryshev.currency.utils.extensions.isDouble
import com.baryshev.currency.utils.extensions.orEmpty
import com.baryshev.currency.utils.extensions.toDoubleOrDefault
import com.baryshev.currency.utils.rx.IRxScheduler

class MainVM(
    private val rxScheduler: IRxScheduler,
    private val mainInteractor: MainInteractor
) : BaseVM() {

    val mainData: MutableLiveData<MainViewResult> = MutableLiveData()
    val amountValidation: MutableLiveData<Boolean> = MutableLiveData()
    @SuppressLint("RxLeakedSubscription")
    fun convert(from: String, to: String, amount: String, needToRefresh: Boolean = true) {

        if (!amount.isDouble()) {
            amountValidation.value = false
            return
        } else {
            amountValidation.value = true
        }

        if (needToRefresh) mainData.value = MainViewResult.Progress

        addDisposable {
            mainInteractor.convert(from.orEmpty { FROM_DEFAULT },
                                   to.orEmpty { TO_DEFAULT },
                                   amount.toDoubleOrDefault { AMOUNT_DEFAULT },
                                   needToRefresh)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribe({ result: MainData.Exchange ->
                               mainData.value = MainViewResult.Success(result)
                           },
                           { throwable: Throwable? ->
                               mainData.value = MainViewResult.Error(throwable?.message.orEmpty(),
                                                                     throwable)
                           })
        }
    }
}