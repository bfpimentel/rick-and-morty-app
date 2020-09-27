package dev.pimentel.rickandmorty.shared.helpers

import dev.pimentel.domain.entities.Pageable

interface PagingHelper<ResultType> {
    fun getCurrentPage(reset: Boolean): Int

    suspend fun handlePaging(
        request: suspend () -> Pageable<ResultType>,
        onSuccess: suspend (List<ResultType>) -> Unit,
        onError: suspend (Throwable) -> Unit
    )
}

class PagingHelperImpl<ResultType> : PagingHelper<ResultType> {

    private var currentPage: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE

    private var allItems: MutableList<ResultType> = mutableListOf()

    override fun getCurrentPage(reset: Boolean): Int {
        if (reset) {
            currentPage = FIRST_PAGE
            lastPage = DEFAULT_LAST_PAGE

            allItems = mutableListOf()
        } else {
            currentPage++
        }

        return currentPage
    }

    override suspend fun handlePaging(
        request: suspend () -> Pageable<ResultType>,
        onSuccess: suspend (List<ResultType>) -> Unit,
        onError: suspend (Throwable) -> Unit
    ) {
        if (currentPage > lastPage) return

        try {
            val response = request()

            allItems.addAll(response.items)
            lastPage = response.lastPage

            onSuccess(allItems)
        } catch (exception: Exception) {
            currentPage = DEFAULT_PAGE
            lastPage = DEFAULT_LAST_PAGE

            onError(exception)
        }
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = Int.MAX_VALUE
    }
}
