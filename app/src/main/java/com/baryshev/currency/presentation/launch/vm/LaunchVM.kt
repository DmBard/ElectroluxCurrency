package com.baryshev.currency.presentation.launch.vm

import android.arch.lifecycle.MutableLiveData
import com.baryshev.currency.R
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.launch.LaunchInteractor
import com.baryshev.currency.presentation.common.BaseVM
import com.baryshev.currency.presentation.common.SimpleViewResult
import com.baryshev.currency.utils.rx.IRxScheduler

class LaunchVM(private val rxScheduler: IRxScheduler,
               private val launchInteractor: LaunchInteractor,
               private val resourceInteractor: IResourceInteractor) : BaseVM() {

    internal val mainData: MutableLiveData<SimpleViewResult> = MutableLiveData()


    fun init() {
        mainData.value = SimpleViewResult.Progress
        val disposable = launchInteractor.addCurrenciesToDBIfNeeds()
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
            .subscribe({
                           mainData.value = SimpleViewResult.Success
                       }, { e: Throwable ->
                           run {
                               mainData.value = SimpleViewResult.Error(resourceInteractor.getString(
                                       R.string.error_common), e)
                           }
                       })
        addDisposable { disposable }
    }
}