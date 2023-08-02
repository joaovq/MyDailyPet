package br.com.joaovq.mydailypet.pet.presentation.viewintent

import android.graphics.Bitmap
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.model.SexType
import java.util.Date

sealed class AddPetAction {
    data class Submit(
        val photoNameFile: String = "",
        val name: String,
        val type: String,
        val weight: Double,
        val sex: SexType,
        val birth: Date?,
        val animal: String,
        val birthAlarm: NotificationAlarmItem,
        val bitmap: Bitmap?,
    ) : AddPetAction()

    data class EditPet(
        val id: Int,
        val photoPath: String = "",
        val name: String,
        val type: String,
        val weight: Double,
        val sex: SexType,
        val birth: Date?,
        val animal: String,
        val birthAlarm: NotificationAlarmItem,
        val bitmap: Bitmap?,
    ) : AddPetAction()
}
