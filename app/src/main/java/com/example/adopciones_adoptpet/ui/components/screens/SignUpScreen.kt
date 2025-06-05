package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.AuthRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.SignUpUserUseCase
import com.example.adopciones_adoptpet.ui.components.viewmodel.SignUpViewModel
import com.example.adopciones_adoptpet.ui.components.views.passwordField
import com.example.adopciones_adoptpet.ui.components.views.textField
import com.example.adopciones_adoptpet.ui.theme.SoftBlue
import com.example.adopciones_adoptpet.utils.ErrorMessages
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
    val darkTheme = isSystemInDarkTheme()


    val context = LocalContext.current
    val tabs = listOf("Adoptante", "Protectora")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val signUpResult = viewModel.signUpResult
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(signUpResult) {
        signUpResult?.onSuccess {
            Toast.makeText(context, context.getString(R.string.user_registered), Toast.LENGTH_SHORT).show()
            navController.navigate("LogInScreen")
            viewModel.clearSignUpResult()
        }?.onFailure {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
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
                                    title,
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
                    textField(stringResource(R.string.name), name) { name = it }
                    textField(stringResource(R.string.email), eMail) { eMail = it }
                    textField(stringResource(R.string.phone), phone) { phone = it }
                    passwordField(stringResource(R.string.password), password) { password = it }
                    if (selectedTabIndex == 1){
                        textField(stringResource(R.string.address), address) { address = it }
                        textField(stringResource(R.string.city), city) { city = it }
                        textField(stringResource(R.string.website), website) { website = it }


                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            val phoneInt = phone.toIntOrNull()
                            if (phoneInt != null) {
                                val role = if (selectedTabIndex == 0) context.getString(R.string.adopter_role) else context.getString(R.string.shelter_role)
                                if (role.equals(context.getString(R.string.adopter_role)))
                                    viewModel.signUpAdopter(name, eMail, password, phoneInt, role)
                                     else
                                    viewModel.signUpShelter(name, eMail, password, phoneInt, role, address,city,website)


                            } else {
                                Toast.makeText(context, ErrorMessages.INVALID_PHONE_NUMBER, Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = name.isNotBlank() && password.isNotBlank() && eMail.isNotBlank() && phone.isNotBlank(),
                        modifier = Modifier
                            .width(200.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text((stringResource(R.string.register)))
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
