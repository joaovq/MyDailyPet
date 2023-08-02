package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import androidx.annotation.StringRes

sealed class AddReminderUiState<out T> {
    data class Success<T>(val data: T, @StringRes val message: Int = br.com.joaovq.core_ui.R.string.message_success) :
        AddReminderUiState<T>()

    object SubmittedSuccess : AddReminderUiState<Nothing>()

    data class Error(
        @StringRes val message: Int = br.com.joaovq.core_ui.R.string.message_error,
        val exception: Exception,
    ) :
        AddReminderUiState<Nothing>()
}
