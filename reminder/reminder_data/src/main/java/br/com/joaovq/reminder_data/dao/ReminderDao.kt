package br.com.joaovq.reminder_data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.joaovq.reminder_data.model.REMINDER_ID_COLUMN_INFO
import br.com.joaovq.reminder_data.model.REMINDER_TABLE_NAME
import br.com.joaovq.reminder_data.model.ReminderDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertReminder(reminderDto: ReminderDto)

    @Query("SELECT * FROM $REMINDER_TABLE_NAME")
    fun selectAll(): Flow<List<ReminderDto>>

    @Transaction
    @Query("SELECT * FROM $REMINDER_TABLE_NAME WHERE $REMINDER_ID_COLUMN_INFO = :id")
    fun selectById(id: Int): Flow<ReminderDto>

    @Update
    suspend fun updateReminder(reminderDto: ReminderDto)

    @Delete
    suspend fun deleteReminder(reminderDto: ReminderDto)

    @Query("DELETE FROM $REMINDER_TABLE_NAME")
    suspend fun deleteAllReminder()
}
