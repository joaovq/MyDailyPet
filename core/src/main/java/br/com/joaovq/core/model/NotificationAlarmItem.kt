package br.com.joaovq.core.model

data class NotificationAlarmItem(
    val timeForNotification: Long,
    val messageNotification: String,
    val description: String,
    val reminderId: Int = 0
) : AlarmItem(timeForNotification, messageNotification)
