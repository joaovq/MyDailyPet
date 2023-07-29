package br.com.joaovq.tasks_domain.usecases

import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetAllTasksUseCase {
    suspend operator fun invoke(): Flow<List<Task>>
}

class GetAllTasks @Inject constructor(
    private val taskRepository: TaskRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetAllTasksUseCase {
    override suspend fun invoke(): Flow<List<Task>> {
        return withContext(dispatcher) {
            taskRepository.getAllTasks()
        }
    }
}
