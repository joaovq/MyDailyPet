package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import br.com.joaovq.mydailypet.reminder.domain.model.Reminder

interface RemindersListState {
    data class Success(val reminders: List<Reminder>) : RemindersListState
    data class Error(val exception: Exception) : RemindersListState
}
