package br.com.joaovq.mydailypet.core.util.permission

interface PermissionManager {

    fun setOnShowRationale(message: String, action: () -> Unit): PermissionManager
    fun checkPermission()
}
