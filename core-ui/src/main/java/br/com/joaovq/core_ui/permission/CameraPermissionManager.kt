package br.com.joaovq.core_ui.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CameraPermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) :
    PermissionManager {
    private val permissions: Permissions = Permissions.Camera
    private var rationale: () -> Unit = {}
    private var registerForActivity: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { requestedPermissionsResults ->
            val allGranted = requestedPermissionsResults.all { it.value }
            action(allGranted)
        }

    override fun setOnShowRationale(action: () -> Unit) = apply {
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
