package br.com.joaovq.mydailypet.core.util.image

import android.graphics.Bitmap
import java.io.File

interface ImageProvider<T, R> {
    suspend fun convert(
        value: T,
        file: File,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    ): R

    suspend fun write(
        value: T,
        path: String?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    ): String?
}
