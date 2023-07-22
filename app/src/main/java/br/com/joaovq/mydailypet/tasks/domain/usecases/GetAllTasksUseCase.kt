package br.com.joaovq.mydailypet.tasks.domain.usecases

import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.mapper.toDomain
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            taskRepository.getAllTasks().map { tasksDtos -> tasksDtos.map { it.toDomain() } }
        }
    }
}
