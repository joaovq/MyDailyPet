package br.com.joaovq.reminder_domain.repository

import br.com.joaovq.reminder_domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getReminderById(id: Int): Flow<Reminder>
    suspend fun insertReminder(reminder: Reminder): Int
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteAll()
}
