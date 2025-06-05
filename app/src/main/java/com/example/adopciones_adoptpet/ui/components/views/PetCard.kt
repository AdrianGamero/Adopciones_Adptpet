package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.utils.FormatPetData.formatAge

@Composable
fun PetCard(
    pet:PetWithImagesAndBreeds,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 35.dp, end = 35.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageSlider(pet.images)
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
        }
    }
}

