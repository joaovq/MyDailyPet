package br.com.joaovq.mydailypet.tasks.domain.usecases

import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.model.Task
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
