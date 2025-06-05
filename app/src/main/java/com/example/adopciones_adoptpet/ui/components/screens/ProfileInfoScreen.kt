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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adopciones_adoptpet.R
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
                    text = stringResource(R.string.name_label, user!!.name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(R.string.email_label, user!!.email),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(R.string.phone_label, user!!.phone),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (user!!.role.equals(stringResource(R.string.shelter_role)) && extraData != null) {
                    Text(
                        text = stringResource(R.string.address_label, extraData!!.address),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.city_label, extraData!!.city),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.website_label, extraData!!.website),
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
                    text = stringResource(R.string.colse_session),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}