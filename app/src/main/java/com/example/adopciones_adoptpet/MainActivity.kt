package com.example.adopciones_adoptpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.FilterRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.GetFiltersUseCase
import com.example.adopciones_adoptpet.ui.components.screens.LogInScreen
import com.example.adopciones_adoptpet.ui.components.screens.SignUpScreen
import com.example.adopciones_adoptpet.ui.components.screens.baseScreen
import com.example.adopciones_adoptpet.ui.components.viewmodel.FilterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val db = AdoptPetDataBase.getDatabase(this)

            val dao = db.petWithImagesDao()
            val repository = FilterRepositoryImpl(dao)
            val useCase = GetFiltersUseCase(repository)
            val viewModel = remember { FilterViewModel(useCase = useCase) }

            NavHost(navController = navController, startDestination = "baseScreen") {
                composable("SignUpScreen") { SignUpScreen(navController = navController) }
                composable("LogInScreen") { LogInScreen(navController = navController) }
                composable("baseScreen"){ baseScreen(viewModel)}

            }
        }
    }
}


@Composable
fun Greeting() {

}

