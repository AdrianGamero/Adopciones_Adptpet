package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.utils.FormatPetData.formatAge

@Composable
fun PetInfo(pet: PetWithImagesAndBreeds) {
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
        Column {

            ImageSlider(pet.images,roundedCorners = false)
            Column(modifier = Modifier.padding(start = 8.dp)) {

                Text(
                    pet.name, style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(stringResource(R.string.age_label, formatAge(pet.age,context)), fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(stringResource(R.string.breed_label, pet.breedName), fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(stringResource(R.string.gender_label, pet.gender.displayName), fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(
                    stringResource(R.string.size_label, pet.size.displayName),
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
                   Text(stringResource(R.string.send_request))
                }

            }

        }
    }
}