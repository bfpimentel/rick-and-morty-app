package dev.pimentel.rickandmorty.shared.views

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioGroup(
    filters: List<String>,
    selectedItemIndex: Int? = null,
    onItemClick: (index: Int) -> Unit
) {
    ScrollableColumn {
        filters.forEachIndexed { index, item ->
            Column(modifier = Modifier.clickable(onClick = { onItemClick(index) })) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    RadioButton(
                        selected = index == selectedItemIndex,
                        onClick = {}
                    )
                    Text(text = item, modifier = Modifier.padding(start = 12.dp))
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
