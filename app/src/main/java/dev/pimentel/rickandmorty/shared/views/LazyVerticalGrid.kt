package dev.pimentel.rickandmorty.shared.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> LazyVerticalGrid(
    items: List<T>,
    perRow: Int,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    LazyVerticalGridForIndexed(
        items = items,
        perRow = perRow
    ) { _, item ->
        itemContent(item)
    }
}

@Composable
fun <T> LazyVerticalGridForIndexed(
    items: List<T>,
    perRow: Int,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    val rows: List<List<T>> = items.withIndex()
        .groupBy { it.index / perRow }
        .map { group -> group.value.map { indexedValue -> indexedValue.value } }

    LazyColumnForIndexed(
        items = rows,
        contentPadding = PaddingValues(all = 8.dp)
    ) { rowIndex, row ->
        Row(modifier = Modifier.fillMaxWidth()) {
            row.forEachIndexed { itemIndex, item ->
                itemContent(rowIndex * perRow + itemIndex, item)
            }
        }
    }
}