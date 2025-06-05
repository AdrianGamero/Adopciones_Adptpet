package com.example.adopciones_adoptpet.ui.components.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adopciones_adoptpet.R
import com.example.adopciones_adoptpet.ui.components.viewmodel.SessionViewModel

@Composable
fun ProfileCard(
    viewModel: SessionViewModel,
    onLoginClick: () -> Unit,
    onProfileClick: () -> Unit
){
    val user by viewModel.loggedUser.collectAsState()

    Box(Modifier.fillMaxWidth().fillMaxHeight(0.1f).clickable(enabled = user != null){onProfileClick()}){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.fillMaxHeight(0.9f)
                .aspectRatio(1f)
                .border(shape = CircleShape, color = Color.LightGray, width = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Login Icon",
                    tint = Color.Gray,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
            }

            if (user == null) {
                Button(onClick = onLoginClick,
                    Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        .fillMaxHeight(0.5f)) {
                    Text(stringResource(R.string.login))
                }
            }else{
                Text(user!!.name, Modifier.padding(start = 16.dp), fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardPreviw(){
}