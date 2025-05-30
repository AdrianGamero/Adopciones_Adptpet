package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds

@Composable
fun PetInfo(pet: PetWithImagesAndBreeds) {
    Box(Modifier.fillMaxWidth().fillMaxHeight()){
        Column {
            ImageSlider(pet.images,roundedCorners = false)
            Column(modifier = Modifier.padding(start = 8.dp)) {

                Text(
                    pet.name, style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text("Edad: ${pet.age}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Raza: ${pet.breedName}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Sexo: ${pet.gender.displayName}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(
                    "Tamaño: ${pet.size.displayName}",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {

                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    androidx.compose.material.Text("Enviar Solicitud")
                }

            }

        }
    }
}