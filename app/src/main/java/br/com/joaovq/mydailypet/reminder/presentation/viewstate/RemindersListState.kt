package br.com.joaovq.mydailypet.reminder.presentation.viewstate

interface RemindersListState {
    data class Success(val reminders: List<br.com.joaovq.reminder_domain.model.Reminder>) :
        RemindersListState
    data class Error(val exception: Exception) : RemindersListState
}
