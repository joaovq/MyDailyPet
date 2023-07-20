package br.com.joaovq.mydailypet.data.local.service.alarm

import android.app.AlarmManager
import br.com.joaovq.mydailypet.data.local.service.alarm.model.AlarmItem

interface AlarmScheduler {
    fun scheduleExactAlarmAllowWhileIdle(alarmItem: AlarmItem)

    fun scheduleRepeatAlarm(
        type: Int = AlarmManager.RTC_WAKEUP,
        alarmItem: AlarmItem,
        interval: Long,
    )

    fun cancel(alarmItem: AlarmItem)
}
