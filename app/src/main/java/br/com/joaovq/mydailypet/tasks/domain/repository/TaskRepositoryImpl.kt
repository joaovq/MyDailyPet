package br.com.joaovq.mydailypet.tasks.domain.repository

import br.com.joaovq.mydailypet.tasks.data.localdatasource.TaskLocalDatasource
import br.com.joaovq.mydailypet.tasks.data.model.TaskDto
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.mapper.toDto
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskLocalDatasource: TaskLocalDatasource,
) : TaskRepository {
    override fun getAllTasks(): Flow<List<TaskDto>> =
        taskLocalDatasource.getAll()

    override fun getTaskById(id: Int): Flow<TaskDto> =
        taskLocalDatasource.getById(id)

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
