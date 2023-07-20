package br.com.joaovq.mydailypet.home.presentation.viewintent

import br.com.joaovq.mydailypet.pet.domain.model.Pet

sealed interface HomeAction {
    object GetPets : HomeAction
    object GetReminders : HomeAction
    data class DeletePet(val pet: Pet) : HomeAction
}
