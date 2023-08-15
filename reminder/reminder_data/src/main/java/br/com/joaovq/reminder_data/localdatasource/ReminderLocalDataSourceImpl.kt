package br.com.joaovq.reminder_data.localdatasource

import br.com.joaovq.core.data.localdatasource.LocalDataSource
import br.com.joaovq.reminder_data.dao.ReminderDao
import br.com.joaovq.reminder_data.mappers.toDto
import br.com.joaovq.reminder_data.model.ReminderDto
import br.com.joaovq.reminder_domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

interface ReminderLocalDataSource : LocalDataSource<ReminderDto> {
    suspend fun insertReminder(reminder: Reminder): Int
}

class ReminderLocalDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao,
) : ReminderLocalDataSource {
    override suspend fun insertReminder(reminder: Reminder): Int {
        return reminderDao.insertReminder(reminder.toDto()).toInt()
    }

    override fun getAll(): Flow<List<ReminderDto>> {
        return try {
            reminderDao.selectAll()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    override fun getById(id: Int): Flow<ReminderDto> {
        return try {
            reminderDao.selectById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    override suspend fun insert(entity: ReminderDto) {
        try {
            reminderDao.insertReminder(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun update(entity: ReminderDto) {
        try {
            reminderDao.updateReminder(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(entity: ReminderDto) {
        try {
            reminderDao.deleteReminder(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteAll() {
        try {
            reminderDao.deleteAllReminder()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
