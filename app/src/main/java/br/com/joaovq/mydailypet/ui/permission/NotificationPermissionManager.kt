package br.com.joaovq.mydailypet.ui.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class NotificationPermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) : PermissionManager {
    private val permission: Permissions = Permissions.Notification
    private var rationaleMessage: String = ""
    private var rationale: () -> Unit = {
    }
    private val registerForActivity =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        }

    override fun setOnShowRationale(message: String, action: () -> Unit): PermissionManager =
        apply {
            this.rationaleMessage = message
            this.rationale = action
        }

    override fun checkPermission() {
        when {
            !permission.permissions.map {
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    it,
                ) == PackageManager.PERMISSION_GRANTED
            }.contains(false) -> {
                action(true)
            }

            fragment.shouldShowRequestPermissionRationale(Permissions.POST_NOTIFICATION) -> {
                rationale()
            }

            else -> registerForActivity.launch(Permissions.Notification.permissions)
        }
    }

    companion object {
        fun from(fragment: Fragment, action: (Boolean) -> Unit = {}) =
            NotificationPermissionManager(fragment = fragment, action)
    }
}
