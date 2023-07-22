package br.com.joaovq.mydailypet.tasks.domain.usecases

import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.model.Task
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
