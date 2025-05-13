package com.example.adopciones_adoptpet.ui.components.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel

@Composable
fun SplashScreen(petViewModel: PetViewModel, onSyncComplete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp).background(Color.Black)
            )
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
    LaunchedEffect(Unit) {
        petViewModel.syncPets()
        onSyncComplete()

    }
}