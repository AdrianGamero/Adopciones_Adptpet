package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.example.adopciones_adoptpet.ui.components.views.WriteableSelectMenu
import com.example.adopciones_adoptpet.ui.components.views.selectMenu
import com.example.adopciones_adoptpet.ui.components.views.textField
import com.example.adopciones_adoptpet.ui.theme.SoftBlue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddPetsScreen(viewModel: PetViewModel, navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    val ageOptions= listOf("Años", "Meses")
    var selectedAgeUnit by remember { mutableStateOf("Años") }


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
        contract = ActivityResultContracts.PickMultipleVisualMedia(4)
    ) { uris ->
        uris?.let { selectedImageUris.addAll(it) }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImageUris.addAll(uris)
    }
    val insertResult by viewModel.insertResult.collectAsState()
    var showErrorDialog by remember { mutableStateOf<String?>(null) }
    val darkTheme = isSystemInDarkTheme()


    LaunchedEffect(selectedTabIndex) {
        viewModel.loadBreeds(selectedPetType)
    }

    LaunchedEffect(insertResult) {
        insertResult?.let {
            if (it.isSuccess) {
                Toast.makeText(context,
                    context.getString(R.string.added_successfully), Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } else {
                showErrorDialog =
                    context.getString(R.string.insert_error, it.exceptionOrNull()?.localizedMessage)
            }
            viewModel.clearInsertResult()
        }
    }

    val breedOptions = breeds.map { it.name }
    var selectedBreed by remember { mutableStateOf("") }
    val onAddClick: () -> Unit = onAddClick@{
        if (name.isBlank() || age.isBlank() || selectedBreed.isBlank() || selectedGender.isBlank() || selectedSize.isBlank()) {
            Toast.makeText(context,
                context.getString(R.string.complete_all_fields), Toast.LENGTH_SHORT).show()
            return@onAddClick
        }

        if (age.toIntOrNull() == null || age.toInt() < 0) {
            Toast.makeText(context, context.getString(R.string.age_input_error), Toast.LENGTH_SHORT).show()
            return@onAddClick
        }

        if (!selectedBreed.matches(Regex(context.getString(R.string.breed_pattern)))) {
            Toast.makeText(context,
                context.getString(R.string.only_text_characters), Toast.LENGTH_SHORT).show()
            return@onAddClick
        }

        if (selectedImageUris.size > 4) {
            showErrorDialog = context.getString(R.string.max_4_images)
            return@onAddClick
        }

        if (selectedAgeUnit == context.getString(R.string.months) && age.toInt() > 11) {
            Toast.makeText(context,
                context.getString(R.string.no_more_than_11_months), Toast.LENGTH_SHORT).show()
            return@onAddClick
        }

        val size = PetSize.entries.firstOrNull { it.displayName == selectedSize } ?: PetSize.SMALL
        val gender = PetGender.entries.firstOrNull { it.displayName == selectedGender } ?: PetGender.MALE

        val bitmaps = selectedImageUris.mapNotNull { uri ->
            try {
                val stream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(stream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        val ageInMonths = when (selectedAgeUnit) {
            context.getString(R.string.years) -> age.toInt() * 12
            context.getString(R.string.months) -> age.toInt()
            else -> age.toInt()
        }

        val pet = PetWithImagesAndBreeds(
            name = name,
            age = ageInMonths,
            gender = gender,
            size = size,
            petType = tabs[selectedTabIndex],
            breedName = selectedBreed,
            images = bitmaps
        )

        viewModel.insertPet(pet)
    }



    Scaffold(
    scaffoldState = scaffoldState,
    content = {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    if (darkTheme) {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = SoftBlue
                        )
                    } else {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title.displayName,
                                color = if (darkTheme && selectedTabIndex == index) SoftBlue
                                else MaterialTheme.colors.onSurface
                            )
                        }
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
                },
                        modifier = Modifier
                            .width(200.dp)
                            .align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(R.string.selec_images_label))
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
                textField(stringResource(R.string.name), name) { name = it }
                Row(){
                    textField(stringResource(R.string.age), age,
                        Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) { age = it }
                    selectMenu(
                        label = "",
                        options = ageOptions,
                        selectedOption = selectedAgeUnit,
                        onOptionSelected = { selectedAgeUnit = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }

                selectMenu(label = stringResource(R.string.size),
                    options = petSizeOptions,
                    selectedOption = selectedSize,
                    onOptionSelected = { selectedSize = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp))
                selectMenu(label = stringResource(R.string.gender),
                    options = petGendersOptions,
                    selectedOption = selectedGender,
                    onOptionSelected = { selectedGender = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp))

                WriteableSelectMenu(
                    label = stringResource(R.string.Breed),
                    options = breedOptions,
                    query = selectedBreed,
                    onQueryChange = { selectedBreed = it },
                    onOptionSelected = {},
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )


                Spacer(modifier = Modifier.height(32.dp))
                Button(

                    onClick = onAddClick,

                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(R.string.add_animal_label))
                }
            }
        }
    }
    )
}