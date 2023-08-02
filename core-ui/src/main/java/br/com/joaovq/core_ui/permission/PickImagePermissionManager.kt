package br.com.joaovq.core_ui.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.joaovq.core_ui.R
import br.com.joaovq.core_ui.extension.goToSettingsAlertDialogForPermission

class PickImagePermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) : PermissionManager {
    private val permissions: Permissions = Permissions.PickImage
    private var rationale: () -> Unit = {
        fragment.goToSettingsAlertDialogForPermission(
            message = R.string.rationale_message_pick_image,
        )
    }
    private var registerForActivity: ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { requestedPermissionsResults ->
            val allGranted = requestedPermissionsResults.all { it.value }
            action(allGranted)
        }

    override fun setOnShowRationale(action: () -> Unit): PermissionManager =
        apply {
            this.rationale = action
        }

    override fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when {
                permissions.permissions.all {
                    ContextCompat.checkSelfPermission(
                        fragment.requireContext(),
                        it,
                    ) == PackageManager.PERMISSION_GRANTED
                } -> {
                    action(true)
                }

                fragment.shouldShowRequestPermissionRationale(
                    Permissions.READ_IMAGES,
                ) -> {
                    rationale()
                }

                else -> registerForActivity.launch(Permissions.PickImage.permissions)
            }
        } else {
            action(true)
        }
    }

    companion object {
        fun from(fragment: Fragment, action: (Boolean) -> Unit) =
            PickImagePermissionManager(fragment, action)
    }
}
