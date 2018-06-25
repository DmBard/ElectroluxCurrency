package com.baryshev.currency.presentation.common

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseVM : ViewModel() {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    protected inline fun addDisposable(code: () -> Disposable) {
        compositeDisposable.add(code())
    }
}