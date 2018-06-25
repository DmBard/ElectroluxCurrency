package com.baryshev.currency

import com.baryshev.currency.utils.rx.IRxScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestRxScheduler : IRxScheduler {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun main(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()
}