package br.com.joaovq.core_ui.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class NotificationPermissionManager private constructor(
    private val fragment: Fragment,
    private val action: (Boolean) -> Unit,
) : PermissionManager {
    private val permission: Permissions = Permissions.Notification
    private var rationale: () -> Unit = {
    }
    private val registerForActivity =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            action(true)
        }

    override fun setOnShowRationale(action: () -> Unit): PermissionManager =
        apply {
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
