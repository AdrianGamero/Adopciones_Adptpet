package com.example.adopciones_adoptpet.components.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun petCard (name:String, images: List<String>){
    Box( modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.width(300.dp)
        ) {
            Text(name)
            imageSlider(images)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun petCardPreview(){
    val mockName= "kyra"
    val mockImages = listOf(
        "https://picsum.photos/id/1015/300/400",
        "https://picsum.photos/id/1016/300/400"
    )
    petCard(mockName,mockImages)
}