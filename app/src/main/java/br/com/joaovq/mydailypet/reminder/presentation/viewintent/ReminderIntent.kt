package br.com.joaovq.mydailypet.reminder.presentation.viewintent

sealed interface ReminderIntent {
    data class EditReminder(
        val id: Int,
        val reminder: br.com.joaovq.reminder_domain.model.Reminder,
    ) : ReminderIntent

    data class GetReminder(
        val id: Int,
    ) : ReminderIntent
    data class DeleteReminder(
        val id: Int,
        val reminder: br.com.joaovq.reminder_domain.model.Reminder,
    ) : ReminderIntent
}
