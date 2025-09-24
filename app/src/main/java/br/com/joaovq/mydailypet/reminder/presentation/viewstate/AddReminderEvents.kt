package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import androidx.annotation.StringRes

sealed interface AddReminderEvents {
    object SubmittedSuccess : AddReminderEvents

    data class Error(
        @StringRes val message: Int = br.com.joaovq.core_ui.R.string.message_error,
        val exception: Throwable,
    ) :
        AddReminderEvents
}
