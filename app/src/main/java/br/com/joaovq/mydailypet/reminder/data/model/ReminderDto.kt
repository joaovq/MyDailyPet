package br.com.joaovq.mydailypet.reminder.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import java.util.Date

const val REMINDER_TABLE_NAME = "reminder_tb"

@Entity(
    tableName = REMINDER_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = PetDto::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["petId"],
            childColumns = ["petId"],
        ),
    ],
    indices = [
        Index("petId"),
    ],
)
data class ReminderDto(
    @PrimaryKey(true)
    @ColumnInfo(name = "id_reminder")
    val id: Int = 0,
    @ColumnInfo(name = "name_reminder")
    val name: String = "",
    val description: String = "",
    val createdAt: Date = Date(),
    @Embedded val pet: PetDto,
    val alarm: NotificationAlarmItem,
    val toDate: Date = Date(),
)
