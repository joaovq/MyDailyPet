package br.com.joaovq.mydailypet.addpet.presentation.viewintent

import br.com.joaovq.mydailypet.pet.domain.model.SexType
import java.util.Date

sealed class AddPetAction {
    data class Submit(
        val photoPath: String = "",
        val name: String,
        val type: String,
        val weight: Double,
        val sex: SexType,
        val birth: Date?,
        val animal: String,
    ) : AddPetAction()
}
