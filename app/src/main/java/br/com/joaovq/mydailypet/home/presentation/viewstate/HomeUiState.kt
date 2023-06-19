package br.com.joaovq.mydailypet.home.presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.pet.domain.model.Pet

sealed class HomeUiState(@StringRes val message: Int) {
    data class Success(val data: List<Pet>) : HomeUiState(R.string.message_success)
    data class Error(val exception: Exception) : HomeUiState(R.string.message_error)
}
