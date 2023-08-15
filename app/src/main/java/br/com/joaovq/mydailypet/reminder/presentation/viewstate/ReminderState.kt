package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import androidx.annotation.StringRes
import br.com.joaovq.reminder_domain.model.Reminder

sealed class ReminderState {
    data class Success(@StringRes val message: Int, val reminder: Reminder? = null) :
        ReminderState()

    object SuccessDelete : ReminderState()
    data class Error(val exception: Exception) : ReminderState()
}
