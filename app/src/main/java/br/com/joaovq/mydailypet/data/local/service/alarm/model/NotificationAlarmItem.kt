package br.com.joaovq.mydailypet.data.local.service.alarm.model

data class NotificationAlarmItem(
    val timeForNotification: Long,
    val messageNotification: String,
    val description: String,
) : AlarmItem(timeForNotification, messageNotification)
