package br.com.joaovq.mydailypet.reminder.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.data.model.PET_ID_COLUMN_INFO
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import java.util.Date

const val REMINDER_TABLE_NAME = "reminder_tb"
const val REMINDER_ID_COLUMN_INFO = "reminder_id"
const val REMINDER_NAME_COLUMN_INFO = "name_reminder"

@Entity(
    tableName = REMINDER_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = PetDto::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = [PET_ID_COLUMN_INFO],
            childColumns = [PET_ID_COLUMN_INFO],
        ),
    ],
    indices = [
        Index(PET_ID_COLUMN_INFO),
    ],
)
data class ReminderDto(
    @PrimaryKey(true)
    @ColumnInfo(name = REMINDER_ID_COLUMN_INFO)
    val id: Int = 0,
    @ColumnInfo(name = REMINDER_NAME_COLUMN_INFO)
    val name: String = "",
    val description: String = "",
    val createdAt: Date = Date(),
    @Embedded val pet: PetDto,
    val alarm: NotificationAlarmItem,
    val toDate: Date = Date(),
)
