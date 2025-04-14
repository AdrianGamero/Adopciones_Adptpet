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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.components.views.passwordField
import com.example.adopciones_adoptpet.components.views.textField
import com.google.firebase.firestore.FirebaseFirestore

fun signUpUser (name: String,userName:String, lastName:String, eMail:String,address:String, password:String, onSuccess:()-> Unit, onError: (Exception) -> Unit){
    val user =hashMapOf("name" to name,"userName" to userName,"lastName" to lastName,"eMail" to eMail,"address" to address, "password" to password)
    val db = FirebaseFirestore.getInstance()
    db.collection("user").add(user).addOnSuccessListener{
        onSuccess()
    }
        .addOnFailureListener { e ->
            onError(e)
        }

}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(){
    val scaffoldState = rememberScaffoldState() // Estado del scaffold
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var eMail by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 100.dp),
            )

            {

                textField("Nombre",name) {name = it }
                textField("Apellidos",lastName) {lastName = it }
                textField("Nombre de usuario",userName) {userName = it }
                textField("E-Mail",eMail) {eMail = it }
                passwordField("Contraseña", password) {password = it}
                textField("Dirección",address) {address = it }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        signUpUser(name,userName,lastName,eMail,address,password,
                            onSuccess = {
                                println("Usuario registrado con éxito")
                            },
                            onError = {
                                println("Error al registrar: ${it.message}")
                            }
                        )                    },
                    enabled = name.isNotBlank() && password.isNotBlank() && lastName.isNotBlank() && userName.isNotBlank() && eMail.isNotBlank() && address.isNotBlank(),
                    modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally)
                ) {
                    Text("Registrarse")
                }
            }

        }

    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreView (){
    SignUpScreen()
}
