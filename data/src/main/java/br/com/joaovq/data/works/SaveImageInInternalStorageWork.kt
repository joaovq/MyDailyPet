package br.com.joaovq.data.works

import android.content.Context
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.core.data.image.BitmapHelperProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class SaveImageInInternalStorageWork @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    @IODispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    override suspend fun doWork(): Result {
        return withContext(coroutineDispatcher) {
            try {
                val bitmapHelperProvider = BitmapHelperProvider(context)
                val path = inputData.getString(PATH_IMAGE_KEY)
                val imageBytes = inputData.getByteArray(BITMAP_IMAGE_KEY)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, 1248)
                if (path != null && bitmap != null) {
                    val imagePath = bitmapHelperProvider.write(bitmap, path)
                    val data = workDataOf(
                        EXTRA_PATH_IMAGE_RECEIVER to
                            imagePath,
                    )
                    Result.success(data)
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }

    companion object {
        const val PATH_IMAGE_KEY = "path-image"
        const val BITMAP_IMAGE_KEY = "bitmap-image"
        const val EXTRA_PATH_IMAGE_RECEIVER = "image-path-saved"
        const val MIN_BACKOFF_MILLIS = 10L
    }
}
