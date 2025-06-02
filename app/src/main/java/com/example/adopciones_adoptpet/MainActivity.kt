package com.example.adopciones_adoptpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.AuthRepositoryImpl
import com.example.adopciones_adoptpet.data.repository.FilterRepositoryImpl
import com.example.adopciones_adoptpet.data.repository.PetRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.AddPetUseCase
import com.example.adopciones_adoptpet.domain.useCase.GetBreedsByTypeUseCase
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import com.example.adopciones_adoptpet.domain.useCase.LogInUseCase
import com.example.adopciones_adoptpet.domain.useCase.SignUpUserUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import com.example.adopciones_adoptpet.ui.components.screens.AddPetsScreen
import com.example.adopciones_adoptpet.ui.components.screens.LogInScreen
import com.example.adopciones_adoptpet.ui.components.screens.SignUpScreen
import com.example.adopciones_adoptpet.ui.components.screens.BaseScreen
import com.example.adopciones_adoptpet.ui.components.screens.ProfileInfoScreen
import com.example.adopciones_adoptpet.ui.components.viewmodel.FilterViewModel
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.example.adopciones_adoptpet.ui.components.viewmodel.SessionViewModel
import com.example.adopciones_adoptpet.ui.components.viewmodel.SignUpViewModel
import com.example.adopciones_adoptpet.ui.theme.Adopciones_AdoptpetTheme
import com.example.adopciones_adoptpet.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Adopciones_AdoptpetTheme {
                val navController = rememberNavController()
                val db = AdoptPetDataBase.getDatabase(this)

                val dao = db.petWithImagesDao()
                val repository = FilterRepositoryImpl(dao)
                val useCase = GetFiltersUseCase(repository)
                val filterViewModel = remember { FilterViewModel(useCase = useCase) }

                val firebaseDb = FirebaseFirestore.getInstance()
                val firebasePetDataSource = FirebasePetDataSource(firebaseDb)
                val roomPetDataSource = RoomPetDataSource(dao)
                val petRepository = PetRepositoryImpl(firebasePetDataSource, roomPetDataSource)
                val syncAndLoadUseCase = SyncAndLoadUseCase(petRepository)
                val getBreedsByTypeUseCase = GetBreedsByTypeUseCase(petRepository)
                val addPetUseCase = AddPetUseCase(petRepository)
                val petViewModel =
                    PetViewModel(syncAndLoadUseCase, getBreedsByTypeUseCase, addPetUseCase)

                val userRemoteDataSource = UserRemoteDataSource()
                val userDao = db.userDao()
                val sessionManager = SessionManager(userDao)
                val authRepository = AuthRepositoryImpl(sessionManager, userRemoteDataSource)
                val logInUseCase = LogInUseCase(authRepository)
                val sessionViewModel = SessionViewModel(logInUseCase)
                val signUpUserUseCase = SignUpUserUseCase(authRepository)
                val signUpViewModel = SignUpViewModel(signUpUserUseCase)

                NavHost(navController = navController, startDestination = "BaseScreen") {
                    composable("SignUpScreen") {
                        SignUpScreen(
                            navController = navController,
                            viewModel = signUpViewModel
                        )
                    }
                    composable("LogInScreen") {
                        LogInScreen(
                            navController = navController,
                            viewModel = sessionViewModel
                        )
                    }
                    composable("BaseScreen") {
                        BaseScreen(
                            filterViewModel,
                            petViewModel,
                            sessionViewModel,
                            navController
                        )
                    }
                    composable("ProfileInfoScreen") {
                        ProfileInfoScreen(
                            sessionViewModel,
                            navController
                        )
                    }
                    composable("AddPetsScreen") { AddPetsScreen(petViewModel, navController) }
                }
            }
        }
    }
}


