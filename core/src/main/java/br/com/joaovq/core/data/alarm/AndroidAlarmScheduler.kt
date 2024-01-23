package br.com.joaovq.core.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import br.com.joaovq.core.data.receivers.DESCRIPTION_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.ID_REMINDER_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.MESSAGE_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.NotificationReceiver
import br.com.joaovq.core.model.AlarmItem
import br.com.joaovq.core.model.NotificationAlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    override fun scheduleExactAlarmAllowWhileIdle(
        alarmItem: AlarmItem,
    ) {
        val alarmIntent =
            Intent(context, NotificationReceiver::class.java).let {
                it.putExtra(MESSAGE_KEY_NOTIFICATION_RECEIVER, alarmItem.message)
                when (alarmItem) {
                    is NotificationAlarmItem -> {
                        it.putExtra(DESCRIPTION_KEY_NOTIFICATION_RECEIVER, alarmItem.description)
                        it.putExtra(ID_REMINDER_KEY_NOTIFICATION_RECEIVER, alarmItem.reminderId)
                    }
                }
                PendingIntent.getBroadcast(
                    context,
                    alarmItem.hashCode(),
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmItem.time,
            alarmIntent,
        )
    }

    override fun scheduleRepeatAlarm(type: Int, alarmItem: AlarmItem, interval: Long) {
        val alarmIntent =
            Intent(context, NotificationReceiver::class.java).let {
                it.putExtra(MESSAGE_KEY_NOTIFICATION_RECEIVER, alarmItem.message)
                when (alarmItem) {
                    is NotificationAlarmItem -> {
                        it.putExtra(DESCRIPTION_KEY_NOTIFICATION_RECEIVER, alarmItem.description)
                    }
                }
                PendingIntent.getBroadcast(
                    context,
                    alarmItem.hashCode(),
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }
        alarmManager.setRepeating(
            type,
            alarmItem.time,
            interval,
            alarmIntent,
        )
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
}
