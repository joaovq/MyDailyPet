package br.com.joaovq.mydailypet.home.presentation.viewintent

sealed interface HomeAction {
    object GetPets : HomeAction
}
