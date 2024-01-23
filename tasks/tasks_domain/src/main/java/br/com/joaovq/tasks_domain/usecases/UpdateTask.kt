package br.com.joaovq.tasks_domain.usecases

import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UpdateTaskUseCase {
    suspend operator fun invoke(id: Int, task: Task, isCompleted: Boolean)
}

class UpdateTask @Inject constructor(
    private val taskRepository: TaskRepository,
    @br.com.joaovq.core.di.DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : UpdateTaskUseCase {
    override suspend fun invoke(id: Int, task: Task, isCompleted: Boolean) {
        withContext(coroutineDispatcher) {
            taskRepository.updateTask(task.copy(id = task.id, isCompleted = isCompleted))
        }
    }
}
