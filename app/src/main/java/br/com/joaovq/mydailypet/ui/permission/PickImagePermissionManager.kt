package br.com.joaovq.mydailypet.ui.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.ui.util.extension.simpleAlertDialog

class PickImagePermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) : PermissionManager {
    private val permissions: Permissions = Permissions.PickImage
    private var rationaleMessage: String = ""
    private var rationale: () -> Unit = {
        fragment.simpleAlertDialog(
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

    override fun setOnShowRationale(message: String, action: () -> Unit): PermissionManager =
        apply {
            this.rationaleMessage = message
            this.rationale = action
        }

    override fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when {
                !permissions.permissions.map {
                    ContextCompat.checkSelfPermission(
                        fragment.requireContext(),
                        it,
                    ) == PackageManager.PERMISSION_GRANTED
                }.contains(false) -> {
                    action(true)
                }

                fragment.shouldShowRequestPermissionRationale(
                    Permissions.READ_IMAGES,
                ) || fragment.shouldShowRequestPermissionRationale(
                    Permissions.WRITE_EXTERNAL_STORAGE,
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