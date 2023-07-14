package br.com.joaovq.mydailypet.data.local.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        /*if (intent?.action == "android.intent.action.BOOT_COMPLETED") {*/
        intent?.extras?.let {
            val notificationService = p0?.let { it1 -> AppNotificationService(it1) }
            notificationService?.sendSmallNotification(
                it.getString("message") ?: "",
                it.getString("description") ?: "",
            )
        }
        /*}*/
    }
}
