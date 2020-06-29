package dev.pimentel.rickandmorty.shared.helpers

import dev.pimentel.domain.entities.Pageable
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import io.reactivex.rxjava3.core.Single

interface PagingHelper<ResultType> {
    fun getCurrentPage(reset: Boolean): Int

    fun Single<Pageable<ResultType>>.handlePaging(
        onSuccess: (List<ResultType>) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun disposePaging()
}

class PagingHelperImpl<ResultType>(
    schedulerProvider: SchedulerProvider
) : PagingHelper<ResultType>,
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider) {

    private var currentPage: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE

    private var allItems: MutableList<ResultType> = mutableListOf()

    override fun disposePaging() {
        disposeHolder()
    }

    override fun getCurrentPage(reset: Boolean): Int {
        if (reset) {
            currentPage = FIRST_PAGE // TODO
            lastPage = DEFAULT_LAST_PAGE

            allItems = mutableListOf()
        } else {
            currentPage++ // TODO
        }

        return currentPage
    }

    override fun Single<Pageable<ResultType>>.handlePaging(
        onSuccess: (List<ResultType>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (this@PagingHelperImpl.currentPage >= this@PagingHelperImpl.lastPage) return

        this.compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                this@PagingHelperImpl.allItems.addAll(response.items)
                this@PagingHelperImpl.lastPage = response.lastPage

                onSuccess(this@PagingHelperImpl.allItems)
            }, { throwable ->
                this@PagingHelperImpl.currentPage = DEFAULT_PAGE // TODO
                this@PagingHelperImpl.lastPage = DEFAULT_LAST_PAGE

                onError(throwable)
            })
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = Int.MAX_VALUE
    }
}
