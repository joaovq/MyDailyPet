package br.com.joaovq.pet_domain.model

import android.os.Parcelable
import br.com.joaovq.core.model.Attach
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.model.SexType
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Pet(
    val id: Int = 0,
    val name: String = "",
    val nickname: String = "",
    val breed: String = "",
    val imageUrl: String = "",
    val animal: String = "",
    val weight: Double = 0.0,
    val birth: Date? = null,
    val sex: SexType = SexType.MALE,
    val attachs: List<Attach> = listOf(),
    val birthAlarm: NotificationAlarmItem,
) : Parcelable
