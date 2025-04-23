package com.example.adopciones_adoptpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.ui.components.screens.LogInScreen
import com.example.adopciones_adoptpet.ui.components.screens.SignUpScreen
import com.example.adopciones_adoptpet.ui.components.screens.baseScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "LogInScreen") {
                composable("SignUpScreen") { SignUpScreen(navController = navController) }
                composable("LogInScreen") { LogInScreen(navController = navController) }
                composable("BaseScreen") { baseScreen(


                ) }
            }
        }
    }
}

@Composable
fun Greeting() {

}

