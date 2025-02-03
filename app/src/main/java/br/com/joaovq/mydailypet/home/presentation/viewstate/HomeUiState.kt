package br.com.joaovq.mydailypet.home.presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.pet_domain.model.Pet

sealed class HomeUiState(@StringRes val message: Int) {
    data class Success(val data: List<Pet>) :
        HomeUiState(br.com.joaovq.core_ui.R.string.message_success)

    data object DeleteSuccess : HomeUiState(br.com.joaovq.core_ui.R.string.message_success)
    data class Error(val exception: Exception) :
        HomeUiState(br.com.joaovq.core_ui.R.string.message_error)
}
