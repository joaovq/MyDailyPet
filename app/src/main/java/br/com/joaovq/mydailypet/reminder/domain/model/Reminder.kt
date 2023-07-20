package br.com.joaovq.mydailypet.reminder.domain.model

import android.os.Parcelable
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Reminder(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val createdAt: Date = Date(),
    val toDate: Date = Date(),
    val pet: Pet,
    val alarmItem: NotificationAlarmItem,
) : Parcelable
