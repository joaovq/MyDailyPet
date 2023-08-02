package br.com.joaovq.reminder_data.repository

import br.com.joaovq.reminder_data.localdatasource.ReminderLocalDataSource
import br.com.joaovq.reminder_data.mappers.toDto
import br.com.joaovq.reminder_data.mappers.toReminder
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderLocalDataSource,
) : ReminderRepository {
    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDataSource.getAll().map { reminders -> reminders.map { it.toReminder() } }
    }

    override fun getReminderById(id: Int): Flow<Reminder> {
        return reminderDataSource.getById(id).map { it.toReminder() }
    }

    override suspend fun insertReminder(reminder: Reminder) {
        reminderDataSource.insert(reminder.toDto())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDataSource.update(reminder.toDto())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDataSource.delete(reminder.toDto())
    }

    override suspend fun deleteAll() {
        reminderDataSource.deleteAll()
    }
}
