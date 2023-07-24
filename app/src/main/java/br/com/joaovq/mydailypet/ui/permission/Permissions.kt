package br.com.joaovq.mydailypet.ui.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
sealed class Permissions(val permissions: Array<String>) {
    object Camera : Permissions(arrayOf(CAMERA))

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object Notification : Permissions(arrayOf(POST_NOTIFICATION))
    object PickImage :
        Permissions(
            arrayOf(
                READ_IMAGES,
            ),
        )

    companion object {
        const val CAMERA = Manifest.permission.CAMERA
        val READ_IMAGES = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS
    }
}
