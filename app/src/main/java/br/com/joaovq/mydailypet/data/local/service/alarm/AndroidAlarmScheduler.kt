package br.com.joaovq.mydailypet.data.local.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import br.com.joaovq.mydailypet.data.local.service.NotificationReceiver
import br.com.joaovq.mydailypet.data.local.service.alarm.model.AlarmItem
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    override fun schedule(alarmItem: AlarmItem) {
        val alarmIntent =
            Intent(context, NotificationReceiver::class.java).let {
                it.putExtra("message", alarmItem.message)
                when (alarmItem) {
                    is NotificationAlarmItem -> {
                        it.putExtra("description", alarmItem.description)
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
