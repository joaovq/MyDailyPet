package br.com.joaovq.mydailypet.pet.presentation.viewstate

import br.com.joaovq.mydailypet.core.domain.model.Pet

sealed class PetState {
    data class Success(val pet: Pet) : PetState()
    object Initial: PetState()
    data class Error(val message: String) : PetState()
}
