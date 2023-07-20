package br.com.joaovq.mydailypet.reminder.presentation.adapter.reminderlist

import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import java.util.Date

sealed interface ReminderListItem {
    data class MarkerDate(val date: Date) : ReminderListItem
    data class ReminderItem(val reminder: Reminder) : ReminderListItem

    companion object {
        const val MARKER_DATE_VIEW_TYPE = 0
        const val REMINDER_VIEW_TYPE = 1
    }
}
