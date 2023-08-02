package br.com.joaovq.core_ui.permission

interface PermissionManager {

    fun setOnShowRationale(action: () -> Unit): PermissionManager
    fun checkPermission()
}
