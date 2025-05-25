package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.AuthRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.SignUpUserUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.SignUpViewModel
import com.example.adopciones_adoptpet.ui.components.views.passwordField
import com.example.adopciones_adoptpet.ui.components.views.textField
import com.example.adopciones_adoptpet.utils.SessionManager


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel) {
    val scaffoldState = rememberScaffoldState()
    var name by remember { mutableStateOf("") }
    var eMail by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    val context = LocalContext.current
    val tabs = listOf("Adoptante", "Protectora")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val signUpResult = viewModel.signUpResult
    LaunchedEffect(signUpResult) {
        signUpResult?.onSuccess {
            Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
            navController.navigate("LogInScreen")
            viewModel.clearSignUpResult()
        }?.onFailure {
            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                ) {
                    textField("Nombre", name) { name = it }
                    textField("E-Mail", eMail) { eMail = it }
                    textField("Teléfono", phone) { phone = it }
                    passwordField("Contraseña", password) { password = it }
                    if (selectedTabIndex == 1){
                        textField("Dirección", address) { address = it }
                        textField("Ciudad", city) { city = it }
                        textField("Página web", website) { website = it }


                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            val phoneInt = phone.toIntOrNull()
                            if (phoneInt != null) {
                                val role = if (selectedTabIndex == 0) "adopter" else "shelter"
                                if (role.equals("adopter"))
                                    viewModel.signUpAdopter(name, eMail, password, phoneInt, role)
                                     else
                                    viewModel.signUpShelter(name, eMail, password, phoneInt, role, address,city,website)


                            } else {
                                Toast.makeText(context, "Número de teléfono inválido", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = name.isNotBlank() && password.isNotBlank() && eMail.isNotBlank() && phone.isNotBlank(),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreView() {
    val context = LocalContext.current

    val db = AdoptPetDataBase.getDatabase(context)
    val dao = db.userDao()
    val userRemoteDataSource = UserRemoteDataSource()
    val sessionManager = SessionManager(dao)
    val authRepository= AuthRepositoryImpl( sessionManager,userRemoteDataSource)
    val signUpUserUseCase= SignUpUserUseCase(authRepository)
    val viewModel= SignUpViewModel(signUpUserUseCase)
    SignUpScreen(navController = rememberNavController(),viewModel)
}
