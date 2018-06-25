package com.baryshev.currency.presentation.main.vm

import android.arch.lifecycle.MutableLiveData
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.domain.main.MainInteractor
import com.baryshev.currency.presentation.common.BaseVM
import com.baryshev.currency.presentation.main.view.MainViewResult
import com.baryshev.currency.utils.extensions.orEmpty
import com.baryshev.currency.utils.rx.IRxScheduler

const val FROM_DEFAULT = "SEK"
const val TO_DEFAULT = "USD"
const val AMMOUNT_DEFAULT = 1.0

class MainVM(
        private val rxScheduler: IRxScheduler,
        private val mainInteractor: MainInteractor,
        private val resourceInteractor: IResourceInteractor
            ) : BaseVM() {

    val mainData: MutableLiveData<MainViewResult> = MutableLiveData()
    val fabEnabledData: MutableLiveData<Boolean> = MutableLiveData()

    fun init(from: String, to: String, ammount: String) {
        mainInteractor.convert(from.orEmpty { FROM_DEFAULT },
                               to.orEmpty { TO_DEFAULT },
                               ammount.toDouble())
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
            .subscribe({ result: MainData.Exchange -> mainData.value = MainViewResult.Success(result) },
                       { throwable: Throwable? ->
                           mainData.value = MainViewResult.Error(throwable?.message.orEmpty(),
                                                                 throwable)
                       })

    }
}