package com.example.adopciones_adoptpet.ui.components.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.data.repository.AuthRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.AddPetUseCase
import com.example.adopciones_adoptpet.domain.useCase.GetBreedsByTypeUseCase
import com.example.adopciones_adoptpet.domain.useCase.LogInUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.SessionViewModel
import com.example.adopciones_adoptpet.ui.components.views.PetCard
import com.example.adopciones_adoptpet.ui.components.views.ProfileCard
import com.example.adopciones_adoptpet.ui.theme.Pink80
import com.example.adopciones_adoptpet.utils.SessionManager


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseScreen(filterViewModel: FilterViewModel, petViewModel: PetViewModel, sessionViewModel: SessionViewModel, navController: NavController

) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val user by sessionViewModel.loggedUser.collectAsState()


    val filters = filterViewModel.filters.value
    val selectedFilters = filterViewModel.selectedFilters.value
    val showFilters = filterViewModel.showFilters.value
    val pets by petViewModel.pets.collectAsStateWithLifecycle()
    var selectedPet by remember { mutableStateOf<PetWithImagesAndBreeds?>(null) }

    val refreshing = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            refreshing.value = true
            coroutineScope.launch {
                petViewModel.syncPets()
                refreshing.value = false
            }
        }
    )
    LaunchedEffect(Unit) {
        petViewModel.syncPets()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val darkTheme = isSystemInDarkTheme()
            TopAppBar(

                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.open_menu))
                    }

                },
                backgroundColor = if (darkTheme) MaterialTheme.colors.primary else Pink80,
                contentColor = if (darkTheme) Color.White else Color.Black
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
                Text(stringResource(R.string.favourites), modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Text(stringResource(R.string.my_requests), modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Text(stringResource(R.string.settings), modifier = Modifier.padding(vertical = 8.dp))
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (selectedPet == null) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                filterViewModel.toggleFilters()
                                filterViewModel.loadFilters(null)
                            },
                            Modifier.padding(start = 8.dp),
                        ) {
                            Text(stringResource(R.string.filters))
                        }
                        if (user?.role.equals(stringResource(R.string.shelter_role))) {
                            Button(
                                onClick = {
                                    navController.navigate("AddPetsScreen")
                                },
                                Modifier.padding(end = 8.dp)
                            ) {
                                Text(stringResource(R.string.add))
                            }
                        }
                    }



                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(pets) { pet ->
                                PetCard(
                                    pet = pet,
                                    onClick = { selectedPet = pet }
                                )
                            }
                        }

                        PullRefreshIndicator(
                            refreshing = refreshing.value,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
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
    val petRepository = PetRepositoryImpl( firebasePetDataSource, roomPetDataSource)
    val syncAndLoadUseCase = SyncAndLoadUseCase(petRepository)
    val getBreedsByTypeUseCase= GetBreedsByTypeUseCase(petRepository)
    val addPetUseCase = AddPetUseCase(petRepository)
    val userRemoteDataSource = UserRemoteDataSource()
    val userDao= db.userDao()
    val sessionManager = SessionManager(userDao)
    val petViewModel = PetViewModel(syncAndLoadUseCase,getBreedsByTypeUseCase, addPetUseCase, sessionManager)
    val authRepository= AuthRepositoryImpl( sessionManager,userRemoteDataSource)
    val logInUseCase= LogInUseCase(authRepository)
    val sessionViewModel= SessionViewModel(logInUseCase)

    BaseScreen(filterViewModel = filterViewModel, petViewModel = petViewModel, sessionViewModel = sessionViewModel, rememberNavController())
}

