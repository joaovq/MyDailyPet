package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.mydailypet.R

sealed class AddReminderUiState<out T> {
    data class Success<T>(val data: T, @StringRes val message: Int = R.string.message_success) :
        AddReminderUiState<T>()

    object SubmittedSuccess : AddReminderUiState<Nothing>()

    data class Error(
        @StringRes val message: Int = R.string.message_error,
        val exception: Exception,
    ) :
        AddReminderUiState<Nothing>()
}
