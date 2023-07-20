package br.com.joaovq.mydailypet.reminder.data.repository

import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<ReminderDto>>
    fun getReminderById(id: Int): Flow<ReminderDto>
    suspend fun insertReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteAll()
}
