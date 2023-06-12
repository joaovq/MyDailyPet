package br.com.joaovq.mydailypet.addpet.presentation.viewintent

import br.com.joaovq.mydailypet.core.domain.model.SexType

sealed class AddPetAction {
    data class Submit(
        val photoPath: String = "",
        val name: String,
        val type: String,
        val weight: Double,
        val sex: SexType,
        val birth: String,
    ) : AddPetAction()
}
