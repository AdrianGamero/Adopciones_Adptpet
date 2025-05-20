package com.example.adopciones_adoptpet.ui.components.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
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
import com.example.adopciones_adoptpet.ui.components.views.passwordField
import com.example.adopciones_adoptpet.ui.components.views.textField
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LogInScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    var eMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
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
                    onClick = {


                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .width(150.dp)
                ) {
                    Text("Registrarse")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 100.dp),
            )

            {


                textField("Email", eMail) { eMail = it }
                passwordField("Password", password) { password = it }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(eMail, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    navController.navigate("BaseScreen")
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    enabled = eMail.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Iniciar sesión")
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogInPreView() {
    LogInScreen(navController = rememberNavController())
}
//fun imageToBase64(bitmap: Bitmap): String {
//    val outputStream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
//    val byteArray = outputStream.toByteArray()
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)
//}





