package dev.pimentel.rickandmorty.shared.helpers

import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable


interface DisposablesHolder {
    fun <T> observeOnUIAfterSingleResult(): SingleTransformer<T, T>
    fun observeOnUIAfterCompletableResult(): CompletableTransformer
    fun <T> Single<T>.handle(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit)
    fun Completable.handle(onSuccess: () -> Unit, onError: (Throwable) -> Unit)
    fun disposeHolder()
}

class DisposablesHolderImpl(
    private val schedulerProvider: SchedulerProvider
) : DisposablesHolder {

    private val compositeDisposable = CompositeDisposable()

    override fun disposeHolder() {
        compositeDisposable.dispose()
    }

    override fun <T> observeOnUIAfterSingleResult() =
        SingleTransformer<T, T> {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }

    override fun observeOnUIAfterCompletableResult() =
        CompletableTransformer {
            it.subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
        }

    override fun <T> Single<T>.handle(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(this.subscribe(onSuccess, onError))
    }

    override fun Completable.handle(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(this.subscribe(onSuccess, onError))
    }
}
