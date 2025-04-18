package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun filterBox(
     selectedFilters:List<String>,
     allFilters: List<String>,
     onFilterSelected: (String) -> Unit
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)){

        }

    }


}