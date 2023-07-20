package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import androidx.annotation.StringRes

sealed class ReminderState {
    data class Success(@StringRes val message: Int) : ReminderState()
    object SuccessDelete : ReminderState()
    data class Error(val exception: Exception) : ReminderState()
}
