package br.com.joaovq.mydailypet.data.local.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val MESSAGE_KEY_NOTIFICATION_RECEIVER = "message"
const val DESCRIPTION_KEY_NOTIFICATION_RECEIVER = "description"

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        intent?.extras?.let {
            val notificationService = p0?.let { it1 -> AppNotificationService(it1) }
            notificationService?.sendSmallNotification(
                it.getString(MESSAGE_KEY_NOTIFICATION_RECEIVER) ?: "",
                it.getString(DESCRIPTION_KEY_NOTIFICATION_RECEIVER) ?: "",
            )
        }
    }
}
