package br.com.joaovq.mydailypet.reminder.presentation.viewintent

import br.com.joaovq.mydailypet.reminder.domain.model.Reminder

sealed interface ReminderIntent {
    data class EditReminder(
        val id: Int,
        val reminder: Reminder,
    ) : ReminderIntent
    data class DeleteReminder(
        val id: Int,
        val reminder: Reminder,
    ) : ReminderIntent
}
