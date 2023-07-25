package br.com.joaovq.mydailypet.reminder.presentation.viewintent

sealed interface ReminderListIntent {
    object GetAllReminders : ReminderListIntent
    object DeleteAllReminders : ReminderListIntent
}
