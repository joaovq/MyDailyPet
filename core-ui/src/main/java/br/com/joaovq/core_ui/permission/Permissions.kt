package br.com.joaovq.core_ui.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
sealed class Permissions(val permissions: Array<String>) {
    object Camera : Permissions(arrayOf(CAMERA))

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object Notification : Permissions(arrayOf(POST_NOTIFICATION))

    companion object {
        const val CAMERA = Manifest.permission.CAMERA
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS
    }
}
