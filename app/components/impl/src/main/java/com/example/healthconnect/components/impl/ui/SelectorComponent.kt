package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> SelectorComponent(
    title: String,
    supportText: String,
    selectedText: String,
    items: List<T>,
    itemComposable: @Composable ColumnScope.(T) -> Unit,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(text = title)
        Box {
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedText)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(text = {
                        Column {
                            itemComposable(item)
                        }
                    }, onClick = {
                        onItemSelected(item)
                        expanded = false
                    })
                }
            }
        }
        Text(text = supportText)
    }
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun SelectorComponentPreview() {
    val sampleItems = listOf("Option 1", "Option 2", "Option 3")

    var selectedText by remember { mutableStateOf("Select an item") }

    SelectorComponent(
        title = "Select an option",
        supportText = "Please choose from the list.",
        selectedText = selectedText,
        items = sampleItems,
        itemComposable = { item ->
            Text(text = item)
        },
        onItemSelected = { selectedItem ->
            selectedText = selectedItem
        }
    )
}
