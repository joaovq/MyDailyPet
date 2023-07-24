package br.com.joaovq.mydailypet.pet.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.domain.model.Attach
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import java.util.Date

const val PET_ID_COLUMN_INFO = "pet_id"
const val IMAGE_URL_COLUMN_INFO = "image_url"
const val PET_TABLE_NAME = "pet_tb"

@Entity(PET_TABLE_NAME)
data class PetDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(PET_ID_COLUMN_INFO)
    val petId: Int = 0,
    val sex: SexType = SexType.MALE,
    val name: String = "",
    val nickname: String = "",
    val breed: String = "",
    val weight: Double = 0.0,
    val birth: Date? = null,
    @ColumnInfo(name = IMAGE_URL_COLUMN_INFO)
    val imageUrl: String = "",
    val animal: String? = "",
    val birthAlarm: NotificationAlarmItem,
    val attachs: List<Attach>? = listOf(),
)
