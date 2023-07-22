package br.com.joaovq.mydailypet.tasks.domain.usecases

import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UpdateTaskUseCase {
    suspend operator fun invoke(id: Int, task: Task, isCompleted: Boolean)
}

class UpdateTask @Inject constructor(
    private val taskRepository: TaskRepository,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : UpdateTaskUseCase {
    override suspend fun invoke(id: Int, task: Task, isCompleted: Boolean) {
        withContext(coroutineDispatcher) {
            taskRepository.updateTask(task.copy(id = task.id, isCompleted = isCompleted))
        }
    }
}
