package br.com.joaovq.tasks_data.repository

import br.com.joaovq.tasks_data.mapper.toDomain
import br.com.joaovq.tasks_data.mapper.toDto
import br.com.joaovq.tasks_domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskLocalDatasource: br.com.joaovq.tasks_data.localdatasource.TaskLocalDatasource,
) : br.com.joaovq.tasks_domain.repository.TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> =
        taskLocalDatasource.getAll().map { tasks -> tasks.map { it.toDomain() } }

    override fun getTaskById(id: Int): Flow<Task> =
        taskLocalDatasource.getById(id).map { it.toDomain() }

    override suspend fun insertTask(task: Task) {
        taskLocalDatasource.insert(task.toDto())
    }

    override suspend fun updateTask(task: Task) {
        taskLocalDatasource.update(task.toDto())
    }

    override suspend fun deleteTask(task: Task) {
        taskLocalDatasource.delete(task.toDto())
    }

    override suspend fun deleteAll() {
        taskLocalDatasource.deleteAll()
    }
}
