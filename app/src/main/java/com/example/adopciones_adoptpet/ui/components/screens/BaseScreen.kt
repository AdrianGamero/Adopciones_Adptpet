package com.example.adopciones_adoptpet.ui.components.screens


//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.FilterRepositoryImpl
import com.example.adopciones_adoptpet.data.repository.PetRepositoryImpl
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.FilterViewModel
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.example.adopciones_adoptpet.ui.components.views.PetInfo
import com.example.adopciones_adoptpet.ui.components.views.filterBox
import com.example.adopciones_adoptpet.ui.components.views.petCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.data.repository.AuthRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.LogInUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.SessionViewModel
import com.example.adopciones_adoptpet.ui.components.views.ProfileCard
import com.example.adopciones_adoptpet.utils.SessionManager


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseScreen(filterViewModel: FilterViewModel, petViewModel: PetViewModel, sessionViewModel: SessionViewModel, navController: NavController

) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val filters = filterViewModel.filters.value
    val selectedFilters = filterViewModel.selectedFilters.value
    val showFilters = filterViewModel.showFilters.value
    val pets by petViewModel.pets.collectAsStateWithLifecycle()
    var selectedPet by remember { mutableStateOf<PetWithImagesAndBreeds?>(null) }


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
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ProfileCard(sessionViewModel,
                    onLoginClick = {navController.navigate("LogInScreen")},
                    onProfileClick = {
                        navController.navigate("ProfileInfoScreen")
                    })
                Divider()
                Text("Opción 2", modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Text("Opción 3", modifier = Modifier.padding(vertical = 8.dp))
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).background(color = Color.LightGray)
            ) {
                if (selectedPet == null) {
                    Button(
                        onClick = {
                            filterViewModel.toggleFilters()
                            filterViewModel.loadFilters(null)
                        },
                        Modifier.padding(start = 8.dp)
                    ) {
                        Text("Filtros")
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(pets) { pet ->
                            petCard(
                                pet = pet,
                                onClick = {selectedPet = pet}
                            )
                        }

                    }

                    if (showFilters) {
                        Dialog(
                            onDismissRequest = {
                                filterViewModel.toggleFilters()
                            },
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
                                        filterViewModel.updateFilter(filterName, selectedOption)
                                    },
                                    onApply = { selected ->
                                        filterViewModel.applyFilters(selected)
                                        petViewModel.applyFilters(selected)
                                    },
                                    onCancel = {
                                        filterViewModel.cancelFilters()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    BackHandler {
                        selectedPet = null
                    }
                    PetInfo(selectedPet!!)
                }
            }
        }
    )
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BaseScreenPreview() {
    val context = LocalContext.current
    val db = AdoptPetDataBase.getDatabase(context)

    val dao = db.petWithImagesDao()
    val filterRepository = FilterRepositoryImpl(dao)
    val filterUseCase = GetFiltersUseCase(filterRepository)
    val filterViewModel = remember { FilterViewModel(useCase = filterUseCase) }
    val firebaseDb = FirebaseFirestore.getInstance()
    val firebasePetDataSource = FirebasePetDataSource(firebaseDb)
    val roomPetDataSource = RoomPetDataSource(dao)
    val petRepository = PetRepositoryImpl(dao, firebasePetDataSource, roomPetDataSource)
    val syncAndLoadUseCase = SyncAndLoadUseCase(petRepository)
    val petViewModel = PetViewModel(syncAndLoadUseCase)
    val userRemoteDataSource = UserRemoteDataSource()
    val userDao= db.userDao()
    val sessionManager = SessionManager(userDao)
    val authRepository= AuthRepositoryImpl( sessionManager,userRemoteDataSource)
    val logInUseCase= LogInUseCase(authRepository)
    val sessionViewModel= SessionViewModel(logInUseCase)

    BaseScreen(filterViewModel = filterViewModel, petViewModel = petViewModel, sessionViewModel = sessionViewModel, rememberNavController())
}

