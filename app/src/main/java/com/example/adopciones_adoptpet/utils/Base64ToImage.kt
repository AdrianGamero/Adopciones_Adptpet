package com.example.adopciones_adoptpet.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Base64ToImage {
    fun decodeBase64(base64: String): Bitmap {
        val decoded = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
    }
}