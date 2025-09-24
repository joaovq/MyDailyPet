package br.com.joaovq.core.data.service

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.core.data.receivers.DESCRIPTION_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.ID_REMINDER_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.MESSAGE_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.model.AlarmItem
import br.com.joaovq.core.model.NotificationAlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidSchedulerManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {

    private val log = Timber.tag(this::class.java.simpleName)

    private val manager = WorkManager
        .getInstance(context)

    override fun scheduleExactAlarmAllowWhileIdle(alarmItem: AlarmItem) {
        val alarmWorkData = getData(alarmItem)
        log.d("Get alarm: ${alarmItem.uuid}")
        val workRequest = OneTimeWorkRequestBuilder<AlarmWork>()
            .setInputData(alarmWorkData)
            .addTag(getTagAlarm(alarmItem))
            .setId(alarmItem.uuid ?: UUID.randomUUID())
            .setInitialDelay(alarmItem.time, timeUnit = TimeUnit.MILLISECONDS)
            .build()
        manager.enqueue(workRequest)
        log.d("enqueue work task notification alarm")
    }

    private fun getTagAlarm(alarmItem: AlarmItem) =
        "alarmWork-${alarmItem.time}-${alarmItem.message}"

    private fun getData(alarmItem: AlarmItem): Data {
        val alarmWorkData = workDataOf(
            MESSAGE_KEY_NOTIFICATION_RECEIVER to alarmItem.message,
        )
        when (alarmItem) {
            is NotificationAlarmItem -> {
                return workDataOf(
                    MESSAGE_KEY_NOTIFICATION_RECEIVER to alarmItem.message,
                    DESCRIPTION_KEY_NOTIFICATION_RECEIVER to alarmItem.description,
                    ID_REMINDER_KEY_NOTIFICATION_RECEIVER to alarmItem.reminderId,
                )
            }
        }
        return alarmWorkData
    }

    override fun scheduleRepeatAlarm(type: Int, alarmItem: AlarmItem, interval: Long) {
        val alarmWorkData = getData(alarmItem)
        val workRequest = PeriodicWorkRequestBuilder<AlarmWork>(interval, TimeUnit.DAYS)
            .setId(alarmItem.uuid ?: UUID.randomUUID())
            .setInputData(alarmWorkData)
            .setInitialDelay(alarmItem.time, timeUnit = TimeUnit.DAYS)
            .build()
        manager.enqueue(workRequest)
    }

    override fun cancel(alarmItem: AlarmItem) {
        val uuid = alarmItem.uuid
        when {
            uuid != null -> {
                manager.cancelWorkById(uuid)
                log.d("cancel work by id: $uuid")
            }

            else -> {
                val alarmTag = getTagAlarm(alarmItem)
                manager.cancelAllWorkByTag(alarmTag)
                log.d("cancel work by tag: $alarmTag")
            }
        }
    }
}
