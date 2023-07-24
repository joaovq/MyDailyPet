package br.com.joaovq.mydailypet.tasks.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.joaovq.mydailypet.pet.data.model.PET_ID_COLUMN_INFO
import br.com.joaovq.mydailypet.tasks.data.model.TASK_ID_COLUMN_INFO
import br.com.joaovq.mydailypet.tasks.data.model.TASK_TABLE_NAME
import br.com.joaovq.mydailypet.tasks.data.model.TaskDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun createTask(taskDto: TaskDto)

    @Query("SELECT * FROM $TASK_TABLE_NAME")
    fun getAll(): Flow<List<TaskDto>>

    @Update
    suspend fun updateTask(taskDto: TaskDto)

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE $TASK_ID_COLUMN_INFO = :id")
    fun getById(id: Int): Flow<TaskDto>

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE $PET_ID_COLUMN_INFO = :petId")
    fun getByPetId(petId: Int): Flow<List<TaskDto>>

    @Delete
    suspend fun deleteTask(taskDto: TaskDto)

    @Query("DELETE FROM $TASK_TABLE_NAME")
    suspend fun deleteAll()
}
