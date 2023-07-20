package br.com.joaovq.mydailypet.reminder.domain.repository

import br.com.joaovq.mydailypet.reminder.data.localdatasource.ReminderLocalDataSource
import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.mappers.toDto
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderLocalDataSource,
) : ReminderRepository {
    override fun getAllReminders(): Flow<List<ReminderDto>> {
        return reminderDataSource.getAll()
    }

    override fun getReminderById(id: Int): Flow<ReminderDto> {
        return reminderDataSource.getById(id)
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
