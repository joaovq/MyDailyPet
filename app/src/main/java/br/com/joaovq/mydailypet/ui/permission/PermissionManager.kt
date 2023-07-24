package br.com.joaovq.mydailypet.ui.permission

interface PermissionManager {

    fun setOnShowRationale(action: () -> Unit): PermissionManager
    fun checkPermission()
}
