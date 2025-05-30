package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.example.adopciones_adoptpet.ui.components.views.passwordField
import com.example.adopciones_adoptpet.ui.components.views.selectMenu
import com.example.adopciones_adoptpet.ui.components.views.textField

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddPetsScreen(viewModel: PetViewModel) {
    val scaffoldState = rememberScaffoldState()
    var name by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }

    val petSizes = PetSize.values().filter { it != PetSize.ALL }
    val petSizeOptions = petSizes.map { it.displayName }
    var selectedSize by remember { mutableStateOf("") }


    val petGenders = PetGender.values().filter { it != PetGender.ALL }
    val petGendersOptions = petGenders.map { it.displayName }
    var selectedGender by remember { mutableStateOf("") }

    val tabs = PetType.values().filter { it != PetType.ALL }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val selectedPetType = tabs[selectedTabIndex]
    val breeds by viewModel.breeds

    LaunchedEffect(selectedTabIndex) {
        viewModel.loadBreeds(selectedPetType)
    }

    val breedOptions = breeds.map { it.name }
    var selectedBreed by remember { mutableStateOf("") }




    Scaffold(
    scaffoldState = scaffoldState,
    content = {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title.displayName) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp)
            ) {
                textField("Nombre", name) { name = it }
                textField("Edad", edad) { edad = it }

                selectMenu(label = "Tamaño",
                    options = petSizeOptions,
                    selectedOption = selectedSize,
                    onOptionSelected = { selectedSize = it },Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))
                selectMenu(label = "Sexo",
                    options = petGendersOptions,
                    selectedOption = selectedGender,
                    onOptionSelected = { selectedGender = it },Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))

                selectMenu(label = "Raza",
                    options = breedOptions,
                    selectedOption = selectedBreed,
                    onOptionSelected = { selectedBreed = it },Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))


                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {

                    },

                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Registrarse")
                }
            }
        }

    }

    )

}