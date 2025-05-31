package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.example.adopciones_adoptpet.ui.components.views.selectMenu
import com.example.adopciones_adoptpet.ui.components.views.textField

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddPetsScreen(viewModel: PetViewModel) {
    val scaffoldState = rememberScaffoldState()
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val petSizes = PetSize.values().filter { it != PetSize.ALL }
    val petSizeOptions = petSizes.map { it.displayName }
    var selectedSize by remember { mutableStateOf("") }
    val context = LocalContext.current


    val petGenders = PetGender.values().filter { it != PetGender.ALL }
    val petGendersOptions = petGenders.map { it.displayName }
    var selectedGender by remember { mutableStateOf("") }

    val tabs = PetType.values().filter { it != PetType.ALL }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val selectedPetType = tabs[selectedTabIndex]
    val breeds by viewModel.breeds
    val selectedImageUris = remember { mutableStateListOf<Uri>() }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5) // hasta 5 imágenes
    ) { uris ->
        uris?.let { selectedImageUris.addAll(it) }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImageUris.addAll(uris)
    }


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
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        galleryLauncher.launch("image/*")
                    }
                }) {
                    Text("Seleccionar imágenes")
                }
                LazyRow {
                    items(selectedImageUris) { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                textField("Nombre", name) { name = it }
                textField("Edad", age) { age = it }

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
                        Log.d("InsertPet", "Botón pulsado")

                        val size = PetSize.values().firstOrNull { it.displayName == selectedSize } ?: PetSize.SMALL
                        val gender = PetGender.values().firstOrNull { it.displayName == selectedGender } ?: PetGender.MALE

                        val bitmaps = selectedImageUris.mapNotNull { uri ->
                            try {
                                val stream = context.contentResolver.openInputStream(uri)
                                BitmapFactory.decodeStream(stream)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }

                        val pet = PetWithImagesAndBreeds(
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            gender = gender,
                            size = size,
                            petType = tabs[selectedTabIndex],
                            breedName = selectedBreed,
                            images = bitmaps
                        )

                        val shelterId = ""

                        viewModel.insertPet(pet, shelterId)
                    },

                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text( "Añadir animal")
                }
            }
        }
    }
    )
}