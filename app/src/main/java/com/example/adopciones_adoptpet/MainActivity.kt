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
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.FilterRepositoryImpl
import com.example.adopciones_adoptpet.data.repository.PetRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
import com.example.adopciones_adoptpet.ui.components.screens.LogInScreen
import com.example.adopciones_adoptpet.ui.components.screens.SignUpScreen
import com.example.adopciones_adoptpet.ui.components.screens.BaseScreen
import com.example.adopciones_adoptpet.ui.components.viewmodel.FilterViewModel
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val db = AdoptPetDataBase.getDatabase(this)

            val dao = db.petWithImagesDao()
            val repository = FilterRepositoryImpl(dao)
            val useCase = GetFiltersUseCase(repository)
            val filterViewModel = remember { FilterViewModel(useCase = useCase) }

            val firebaseDb = FirebaseFirestore.getInstance()
            val firebasePetDataSource = FirebasePetDataSource(firebaseDb)
            val roomPetDataSource = RoomPetDataSource(dao)
            val petRepository= PetRepositoryImpl(dao,firebasePetDataSource,roomPetDataSource)
            val syncAndLoadUseCase= SyncAndLoadUseCase(petRepository)
            val petViewModel = PetViewModel(syncAndLoadUseCase)


            NavHost(navController = navController, startDestination = "baseScreen") {
                composable("SignUpScreen") { SignUpScreen(navController = navController) }
                composable("LogInScreen") { LogInScreen(navController = navController) }
                composable("baseScreen"){ BaseScreen(filterViewModel,petViewModel)}

            }
        }
    }
}


