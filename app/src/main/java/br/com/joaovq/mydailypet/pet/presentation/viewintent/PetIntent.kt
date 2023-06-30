package br.com.joaovq.mydailypet.pet.presentation.viewintent

sealed class PetIntent {
    data class GetPetDetails(val id: Int) : PetIntent()
}
