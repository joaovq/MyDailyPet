package br.com.joaovq.mydailypet.reminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertReminder(reminderDto: ReminderDto)

    @Query("SELECT * FROM reminder_tb")
    fun selectAll(): Flow<List<ReminderDto>>

    @Transaction
    @Query("SELECT * FROM reminder_tb WHERE id_reminder = :id")
    fun selectById(id: Int): Flow<ReminderDto>

    @Update
    suspend fun updateReminder(reminderDto: ReminderDto)

    @Delete
    suspend fun deleteReminder(reminderDto: ReminderDto)

    @Query("DELETE FROM reminder_tb")
    suspend fun deleteAllReminder()
}
