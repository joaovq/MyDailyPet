package br.com.joaovq.mydailypet.reminder.presentation.adapter.reminderlist

import java.util.Date

sealed interface ReminderListItem {
    data class MarkerDate(val date: Date) : ReminderListItem
    data class ReminderItem(val reminder: br.com.joaovq.reminder_domain.model.Reminder) :
        ReminderListItem

    companion object {
        const val MARKER_DATE_VIEW_TYPE = 0
        const val REMINDER_VIEW_TYPE = 1
    }
}
