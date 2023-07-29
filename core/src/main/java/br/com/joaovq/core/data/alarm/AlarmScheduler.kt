package br.com.joaovq.core.data.alarm

import android.app.AlarmManager
import br.com.joaovq.core.model.AlarmItem

interface AlarmScheduler {
    fun scheduleExactAlarmAllowWhileIdle(alarmItem: AlarmItem)

    fun scheduleRepeatAlarm(
        type: Int = AlarmManager.RTC_WAKEUP,
        alarmItem: AlarmItem,
        interval: Long,
    )

    fun cancel(alarmItem: AlarmItem)
}
