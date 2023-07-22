package br.com.joaovq.mydailypet.tasks.data.repository

import br.com.joaovq.mydailypet.tasks.data.model.TaskDto
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskDto>>
    fun getTaskById(id: Int): Flow<TaskDto>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAll()
}