package br.com.joaovq.tasks_domain.usecases

import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CreateTaskUseCase {
    suspend operator fun invoke(task: Task)
}

class CreateTask @Inject constructor(
    private val taskRepository: TaskRepository,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : CreateTaskUseCase {
    override suspend fun invoke(task: Task) {
        withContext(coroutineDispatcher) {
            taskRepository.insertTask(task)
        }
    }
}
