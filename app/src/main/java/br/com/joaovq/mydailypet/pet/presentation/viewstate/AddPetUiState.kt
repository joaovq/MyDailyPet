package br.com.joaovq.mydailypet.pet.presentation.viewstate

import androidx.annotation.StringRes

data class AddPetUiState(
    val isLoading: Boolean,
    @StringRes val message: Int? = null,
    val isSuccesful: Boolean = false,
    val pathImage: String = "",
    val exception: Exception? = null,
)
