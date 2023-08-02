package br.com.joaovq.mydailypet.pet.presentation.viewstate

import br.com.joaovq.pet_domain.model.Pet

sealed class PetState {
    data class Success(val pet: br.com.joaovq.pet_domain.model.Pet) : PetState()
    data class Error(val message: String) : PetState()
}
