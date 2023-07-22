package br.com.joaovq.mydailypet.pet.domain.usecases

import android.graphics.Bitmap
import br.com.joaovq.mydailypet.core.util.image.ImageProvider
import br.com.joaovq.mydailypet.di.DefaultDispatcher
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
