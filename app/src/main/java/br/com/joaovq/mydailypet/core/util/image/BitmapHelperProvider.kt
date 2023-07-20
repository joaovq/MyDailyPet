package br.com.joaovq.mydailypet.core.util.image

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BitmapHelperProvider(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ImageProvider<Bitmap, File?> {
    override suspend fun convert(value: Bitmap, file: File, format: Bitmap.CompressFormat): File? {
        return withContext(Dispatchers.IO) {
            val outputStream = FileOutputStream(file)
            value.compress(format, 95, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absoluteFile
        }
    }

    override suspend fun write(
        value: Bitmap,
        path: String?,
        format: Bitmap.CompressFormat,
    ): Boolean {
        return withContext(dispatcher) {
            context.openFileOutput(path, Context.MODE_PRIVATE).use { stream ->
                try {
                    if (!value.compress(format, 95, stream)) {
                        throw IOException()
                    }
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
        }
    }
}
