package dev.pimentel.rickandmorty.testshared

import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler

internal class TestSchedulerProvider(
    testScheduler: TestScheduler
) : SchedulerProvider {

    override val ui: Scheduler = testScheduler

    override val io: Scheduler = testScheduler
}
