package br.com.joaovq.core.model

import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class NotificationAlarmItem(
    val timeForNotification: Long,
    val messageNotification: String,
    val description: String,
    val reminderId: Int = 0,
    val id: UUID?
) : AlarmItem(timeForNotification, messageNotification, id)
