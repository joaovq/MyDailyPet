package br.com.joaovq.tasks_domain.repository

import br.com.joaovq.tasks_domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTaskById(id: Int): Flow<Task>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAll()
}
