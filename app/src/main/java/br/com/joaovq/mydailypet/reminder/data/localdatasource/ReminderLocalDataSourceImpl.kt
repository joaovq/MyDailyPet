package br.com.joaovq.mydailypet.reminder.data.localdatasource

import br.com.joaovq.mydailypet.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.reminder.data.dao.ReminderDao
import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

interface ReminderLocalDataSource : LocalDataSource<ReminderDto> {
}

class ReminderLocalDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao,
) : ReminderLocalDataSource {

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
