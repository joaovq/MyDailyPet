package br.com.joaovq.reminder_domain.model

import android.os.Parcelable
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.pet_domain.model.Pet
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
