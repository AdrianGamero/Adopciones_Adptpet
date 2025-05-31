package com.example.adopciones_adoptpet.ui.components.views

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds

@Composable
fun petCard(
    pet:PetWithImagesAndBreeds,
    onClick: () -> Unit
) {
    fun formatAge(months: Int): String {
        return when {
            months < 12 -> "$months meses"
            months % 12 == 0 -> "${months / 12} años"
            else -> "${months / 12} años y ${months % 12} meses"
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 35.dp, end = 35.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(24.dp))
            .clickable{onClick()},
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
                Text("Edad: ${formatAge(pet.age)}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Raza: ${pet.breedName}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Sexo: ${pet.gender.displayName}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(
                    "Tamaño: ${pet.size.displayName}",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun petCardPreview() {


}