package br.com.joaovq.pet_domain.usecases

import android.graphics.Bitmap
import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.core.data.image.ImageProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

interface SaveImagePetUseCase {
    suspend operator fun invoke(bitmap: Bitmap, path: String): String?
}

class SaveImageInInternalStorage @Inject constructor(
    private val bitmapHelperProvider: ImageProvider<Bitmap, File?>,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : SaveImagePetUseCase {
    override suspend fun invoke(bitmap: Bitmap, path: String): String? {
        return withContext(coroutineDispatcher) {
            bitmapHelperProvider.write(value = bitmap, path = path)
        }
    }
}
