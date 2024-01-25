package br.com.joaovq.core.data.service

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.core.data.receivers.DESCRIPTION_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.ID_REMINDER_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.MESSAGE_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.model.AlarmItem
import br.com.joaovq.core.model.NotificationAlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidSchedulerManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {
    override fun scheduleExactAlarmAllowWhileIdle(alarmItem: AlarmItem) {
        val alarmWorkData = getData(alarmItem)
        val id = UUID.randomUUID()
        val workRequest = OneTimeWorkRequestBuilder<AlarmWork>()
            .setInputData(
                alarmWorkData
            )
            .addTag("alarmWork")
            .setId(id)
            .setInitialDelay(alarmItem.time, timeUnit = TimeUnit.MILLISECONDS)
            .build()
        val workManager = WorkManager
            .getInstance(context)
        workManager
            .enqueue(workRequest)
    }

    private fun getData(alarmItem: AlarmItem): Data {
        val alarmWorkData = workDataOf(
            MESSAGE_KEY_NOTIFICATION_RECEIVER to alarmItem.message,
        )
        when (alarmItem) {
            is NotificationAlarmItem -> {
                Log.e("Reminder ID", alarmItem.reminderId.toString())
                return workDataOf(
                    MESSAGE_KEY_NOTIFICATION_RECEIVER to alarmItem.message,
                    DESCRIPTION_KEY_NOTIFICATION_RECEIVER to alarmItem.description,
                    ID_REMINDER_KEY_NOTIFICATION_RECEIVER to alarmItem.reminderId
                )
            }
        }
        return alarmWorkData
    }

    override fun scheduleRepeatAlarm(type: Int, alarmItem: AlarmItem, interval: Long) {
        TODO("Not yet implemented")
    }

    override fun cancel(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }
}