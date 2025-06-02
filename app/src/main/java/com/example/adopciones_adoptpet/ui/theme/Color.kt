package com.example.adopciones_adoptpet.ui.theme


import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Pink80 = Color(0xFFFFDCE2)
val Pink60 = Color(0xFFFFA6B0)
val PinkGrey40 = Color(0xFF7A5A5A)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val SoftBlue = Color(0xFF6A7BA2)
val LightGrayBackground = Color(0xFFF2F2F2)
val DarkGrayBackground = Color(0xFF1C1C1E)


val LightColors = lightColors(
    primary = Pink60,
    primaryVariant = PinkGrey40,
    onPrimary = Black,
    background = LightGrayBackground,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

val DarkColors = darkColors(
    primary = SoftBlue,
    primaryVariant = PinkGrey40,
    onPrimary = White,
    background = DarkGrayBackground,
    onBackground = White,
    surface = Color(0xFF121212),
    onSurface = White
)