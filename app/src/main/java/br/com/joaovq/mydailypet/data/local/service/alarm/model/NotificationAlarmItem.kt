package br.com.joaovq.mydailypet.data.local.service.alarm.model

data class NotificationAlarmItem(
    override val time: Long,
    override val message: String,
    val description: String,
) : AlarmItem(time, message)
