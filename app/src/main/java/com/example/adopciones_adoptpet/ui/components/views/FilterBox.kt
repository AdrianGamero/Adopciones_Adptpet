package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun filterBox(
    selectedFilters: Map<String, String>,
    allFilters: Map<String, List<String>>,
    onFilterSelected: (filterName: String, selectedOption: String) -> Unit,
    onApply: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var tempFilters by remember { mutableStateOf(selectedFilters.toMutableMap()) }
    LaunchedEffect(selectedFilters) {
        tempFilters = selectedFilters.toMutableMap()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            allFilters.forEach { (filterName, options) ->
                val selectedOption = selectedFilters[filterName] ?: ""
                Spacer(modifier = Modifier.height(8.dp))

                selectMenu(label = filterName,
                    options = options,
                    selectedOption = selectedOption,
                    onOptionSelected = { selected ->
                        onFilterSelected(filterName, selected)
                    },
                    Modifier.fillMaxWidth()
                    )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCancel
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { onApply(tempFilters) },
                ) {
                    Text("Aplicar")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun filterBoxPreview() {
    val allFilters = mapOf(
        "Categoría" to listOf("Perro", "Gato", "Otro"),
        "Tamaño" to listOf("Pequeño", "Mediano", "Grande")
    )

    var selectedFilters by remember {
        mutableStateOf(
            mapOf(
                "Categoría" to "Perro",
                "Tamaño" to "Mediano"
            )
        )
    }

    filterBox(
        selectedFilters = selectedFilters,
        allFilters = allFilters,
        onFilterSelected = { filterName, selectedOption ->
            selectedFilters = selectedFilters.toMutableMap().also {
                it[filterName] = selectedOption
            }
        },
        onApply = { filters ->
            selectedFilters = filters
        },
        onCancel = {

        }
    )
}

