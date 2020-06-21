package dev.pimentel.rickandmorty.shared.helpers

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EndOfScrollListener<T : RecyclerView.LayoutManager> constructor(
    private val layoutManager: T,
    private val isLoading: () -> Boolean,
    private val isLastPage: () -> Boolean,
    private val loadMoreItems: () -> Unit
) : RecyclerView.OnScrollListener() {

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
        if (isLoading() || isLastPage()) {
            return
        }

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
            firstVisibleItemPosition >= 0
        ) {
            loadMoreItems()
        }
    }
}
