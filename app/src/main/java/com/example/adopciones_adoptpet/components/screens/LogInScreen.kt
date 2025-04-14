package com.example.adopciones_adoptpet.components.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.components.views.passwordField
import com.example.adopciones_adoptpet.components.views.textField
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LogInScreen(){
    val scaffoldState = rememberScaffoldState() // Estado del scaffold
    val scope = rememberCoroutineScope() // Coroutines para gestionar la apertura del menú
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .width(150.dp)
                ) {
                    Text("Registrarse")
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 100.dp),
            )

            {


                textField("UserName",name) {name = it }
                passwordField("Password", password) {password = it}
                Spacer(modifier = Modifier.height(32.dp))
                 Button(
                     onClick = {
                     },
                     enabled = name.isNotBlank() && password.isNotBlank(),
                     modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally)
                 ) {
                     Text("Iniciar sesión")
                 }
            }

        }

    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogInPreView (){
    LogInScreen()
}


