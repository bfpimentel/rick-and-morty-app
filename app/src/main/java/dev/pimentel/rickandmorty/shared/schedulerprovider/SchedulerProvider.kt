package dev.pimentel.rickandmorty.shared.schedulerprovider

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


interface SchedulerProvider {
    val io: Scheduler
    val ui: Scheduler
}

class SchedulerProviderImpl : SchedulerProvider {

    override val io: Scheduler = Schedulers.io()

    override val ui: Scheduler = AndroidSchedulers.mainThread()
}
