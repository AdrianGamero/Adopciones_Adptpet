package com.example.adopciones_adoptpet.ui.components.views

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun petCard(
    name: String,
    images: MutableList<Bitmap>,
    age: Int,
    race: String,
    size: String,
    gender: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 35.dp, end = 35.dp)
            .border(width = 1.dp, color = Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(321.dp)
        ) {
            imageSlider(images)
            Column(modifier = Modifier.padding(start = 8.dp)) {

                Text(
                    name, style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text("Edad: ${age}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Raza: ${race}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text("Sexo: ${gender}", fontSize = 17.sp, modifier = Modifier.padding(top = 8.dp))
                Text(
                    "Tamaño: ${size}",
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
    val mockName = "kyra"
    val mockImages = listOf(
        "https://picsum.photos/id/1015/321/400",
        "https://picsum.photos/id/1016/321/400"
    )
    val age = 8
    val race = "Jack Rusell"
    val size = "Pequeño"
    val gender = "Hembra"

}