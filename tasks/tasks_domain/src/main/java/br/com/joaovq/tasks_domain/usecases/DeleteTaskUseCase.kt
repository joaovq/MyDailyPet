package br.com.joaovq.tasks_domain.usecases

import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeleteTaskUseCase {
    suspend operator fun invoke(taskId: Int, task: Task)
}

class DeleteTask @Inject constructor(
    private val taskRepository: TaskRepository,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : DeleteTaskUseCase {
    override suspend fun invoke(taskId: Int, task: Task) {
        withContext(coroutineDispatcher) {
            taskRepository.deleteTask(task.copy(id = taskId))
        }
    }
}
