package br.com.joaovq.mydailypet.core.util.image

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class BitmapWriterProvider : ImageProvider<Bitmap, File?> {
    override fun convert(value: Bitmap, path: String?, format: String): File? {
        val file = path?.let { safeFile -> File(safeFile) }
        val outputStream = FileOutputStream(file)
        val compressFormat = when (format) {
            "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG
            "png" -> Bitmap.CompressFormat.PNG
            else -> {
                null
            }
        }
        value.compress(compressFormat, 50, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}
