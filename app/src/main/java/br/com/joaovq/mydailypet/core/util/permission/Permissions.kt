package br.com.joaovq.mydailypet.core.util.permission

import android.Manifest
import android.os.Build

sealed class Permissions(val permissions: Array<String>) {
    object Camera : Permissions(arrayOf(CAMERA))
    object PickImage :
        Permissions(arrayOf(READ_IMAGES, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE))

    companion object {
        const val CAMERA = Manifest.permission.CAMERA
        val READ_IMAGES = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
