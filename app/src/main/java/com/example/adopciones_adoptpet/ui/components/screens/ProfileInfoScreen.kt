package com.example.adopciones_adoptpet.ui.components.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adopciones_adoptpet.ui.components.viewmodel.SessionViewModel

@Composable
fun ProfileInfoScreen(viewModel: SessionViewModel, navController: NavController) {
    val user by viewModel.loggedUser.collectAsState()
    val extraData by viewModel.shelterExtraData.collectAsState()

    if (user == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Nombre: ${user!!.name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Email: ${user!!.email}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Teléfono: ${user!!.phone}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (user!!.role == "shelter" && extraData != null) {
                    Text(
                        text = "Dirección: ${extraData!!.address}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Ciudad: ${extraData!!.city}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Página web: ${extraData!!.website}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.clearSession()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                border = BorderStroke(2.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}