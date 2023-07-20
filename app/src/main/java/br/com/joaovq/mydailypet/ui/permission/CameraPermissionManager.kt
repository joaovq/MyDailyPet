package br.com.joaovq.mydailypet.ui.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CameraPermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) :
    PermissionManager {
    private val permissions: Permissions = Permissions.Camera
    private var rationaleMessage: String = ""
    private var rationale: () -> Unit = {}
    private var registerForActivity: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { requestedPermissionsResults ->
            val allGranted = requestedPermissionsResults.all { it.value }
            action(allGranted)
        }

    fun showRationale() {
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(RATIONALE_TITLE)
            .setMessage(rationaleMessage)
            .setPositiveButton(RATIONALE_POSITIVE_BUTTON) { dialog, _ ->
                val intent =
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        "package:${fragment.activity?.packageName}".toUri(),
                    )
                fragment.activity?.startActivity(intent)
            }.setNegativeButton(RATIONALE_NEGATIVE_BUTTON) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun setOnShowRationale(message: String, action: () -> Unit) = apply {
        this.rationaleMessage = message
        this.rationale = action
    }

    override fun checkPermission() {
        when {
            !permissions.permissions.map {
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    it,
                ) == PackageManager.PERMISSION_GRANTED
            }.contains(false) -> {
                action(true)
            }

            fragment.shouldShowRequestPermissionRationale(Permissions.CAMERA) -> {
                rationale()
            }

            else -> registerForActivity.launch(Permissions.Camera.permissions)
        }
    }

    companion object {
        fun from(fragment: Fragment, action: (Boolean) -> Unit) =
            CameraPermissionManager(fragment = fragment, action)

        const val RATIONALE_TITLE = "Camera use"
        const val RATIONALE_POSITIVE_BUTTON = "SETTINGS"
        const val RATIONALE_NEGATIVE_BUTTON = "CANCEL"
    }
}
