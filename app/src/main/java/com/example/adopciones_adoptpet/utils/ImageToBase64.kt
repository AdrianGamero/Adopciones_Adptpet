package com.example.adopciones_adoptpet.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageToBase64 {
    private const val MAX_SIZE_BYTES = 750_000

    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        var quality = 100
        var compressedBytes: ByteArray

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedBytes = outputStream.toByteArray()
            quality -= 5
        } while (compressedBytes.size > MAX_SIZE_BYTES && quality > 10)

        if (compressedBytes.size > MAX_SIZE_BYTES) {
            val maxWidth = 600
            val aspectRatio = bitmap.height.toDouble() / bitmap.width.toDouble()
            val newHeight = (maxWidth * aspectRatio).toInt()
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true)

            quality = 100
            do {
                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                compressedBytes = outputStream.toByteArray()
                quality -= 5
            } while (compressedBytes.size > MAX_SIZE_BYTES && quality > 10)

        }

        return Base64.encodeToString(compressedBytes, Base64.DEFAULT)
    }
}