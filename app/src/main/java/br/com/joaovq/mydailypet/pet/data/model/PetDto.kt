package br.com.joaovq.mydailypet.pet.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.domain.model.Attach
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import java.util.Date

@Entity("pet_tb")
data class PetDto(
    @PrimaryKey(autoGenerate = true)
    val petId: Int = 0,
    val sex: SexType = SexType.NOT_IDENTIFIED,
    val name: String = "",
    val nickname: String = "",
    val breed: String = "",
    val weight: Double = 0.0,
    val birth: Date? = null,
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",
    val animal: String? = "",
    val birthAlarm: NotificationAlarmItem,
    val attachs: List<Attach>? = listOf(),
)
