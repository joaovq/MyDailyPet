package br.com.joaovq.core.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.joaovq.core.data.notification.AppNotificationService
import br.com.joaovq.core.presentation.NavLinksApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val MESSAGE_KEY_NOTIFICATION_RECEIVER = "message"
const val DESCRIPTION_KEY_NOTIFICATION_RECEIVER = "description"
const val ID_REMINDER_KEY_NOTIFICATION_RECEIVER = "reminder-id"

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var navLinksApp: NavLinksApp
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.extras?.let {
            val notificationService =
                context?.let { safeContext -> AppNotificationService(safeContext) }
            val message = it.getString(MESSAGE_KEY_NOTIFICATION_RECEIVER) ?: ""
            val description = it.getString(DESCRIPTION_KEY_NOTIFICATION_RECEIVER) ?: ""
            val idReminder = it.getInt(ID_REMINDER_KEY_NOTIFICATION_RECEIVER, 0)
            notificationService?.sendSmallNotification(
                message,
                description,
                contentIntent = if (idReminder == 0) {
                    null
                } else {
                    navLinksApp.getToReminderDetailsIntent(
                        idReminder,
                    )
                },
            )
        }
    }
}
