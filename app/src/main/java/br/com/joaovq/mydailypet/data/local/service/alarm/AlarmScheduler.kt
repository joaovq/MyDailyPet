package br.com.joaovq.mydailypet.data.local.service.alarm

import br.com.joaovq.mydailypet.data.local.service.alarm.model.AlarmItem

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}
