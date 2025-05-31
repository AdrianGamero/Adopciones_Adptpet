package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WriteableSelectMenu(
    label: String,
    options: List<String>,
    query: String,
    onQueryChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredOptions = options.filter {
        it.contains(query, ignoreCase = true)
    }

    Box(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = query,
                onValueChange = {
                    onQueryChange(it)
                    expanded = true
                },
                label = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded && filteredOptions.isNotEmpty(),
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .exposedDropdownSize()
                    .heightIn(max = 150.dp)
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        onQueryChange(option)
                        expanded = false
                    }) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}
