package br.com.joaovq.mydailypet.ui.permission

interface PermissionManager {

    fun setOnShowRationale(message: String, action: () -> Unit): PermissionManager
    fun checkPermission()
}
