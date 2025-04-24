package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.FilterRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.FilterViewModel
import com.example.adopciones_adoptpet.ui.components.views.filterBox
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun baseScreen(viewModel: FilterViewModel) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val filters = viewModel.filters.value
    val selectedFilters = viewModel.selectedFilters.value
    val showFilters = viewModel.showFilters.value
    val resultText = viewModel.resultText.value



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .padding(16.dp)

            ) {
                Text("Opción 1", modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Text("Opción 2", modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Text("Opción 3", modifier = Modifier.padding(vertical = 8.dp))
            }
        },
        content = { paddingValues ->

            Button(onClick = {
                viewModel.toggleFilters()
                viewModel.loadFilters(null)
            }) {
                Text("Filtros")
            }
            Text(resultText)
            if (showFilters) {
                Dialog(
                    onDismissRequest = { viewModel.showFilters },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        filterBox(
                            selectedFilters = selectedFilters,
                            allFilters = filters.associate { it.name to it.options },
                            onFilterSelected = { filterName, selectedOption ->
                                viewModel.updateFilter(filterName, selectedOption)
                            },
                            onApply = { selected ->
                                viewModel.applyFilters(selected)
                            },
                            onCancel = {
                                viewModel.cancelFilters()
                            }
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun baseScreenPreview() {
    val context = LocalContext.current
    val db = AdoptPetDataBase.getDatabase(context)

    val dao = db.petWithImagesDao()
    val repository = FilterRepositoryImpl(dao)
    val useCase = GetFiltersUseCase(repository)
    val viewModel = remember { FilterViewModel(useCase = useCase) }

    baseScreen(viewModel = viewModel)
}

