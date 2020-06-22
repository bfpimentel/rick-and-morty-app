package dev.pimentel.rickandmorty.shared.helpers

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class EndOfScrollListener<T : RecyclerView.LayoutManager> constructor(
    private val layoutManager: T,
    private val isLoading: () -> Boolean,
    private val isLastPage: () -> Boolean,
    private val loadMoreItems: () -> Unit
) : RecyclerView.OnScrollListener() {

    private val loadMoreItemsPublisher = PublishSubject.create<Unit>()
    private val disposable: Disposable = loadMoreItemsPublisher
        .filter { !isLoading() && !isLastPage() }
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .subscribe { loadMoreItems() }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null)[0]
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> throw IllegalArgumentException()
        }

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
            firstVisibleItemPosition >= 0
        ) {
            loadMoreItemsPublisher.onNext(Unit)
        }
    }

    fun dispose() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}
