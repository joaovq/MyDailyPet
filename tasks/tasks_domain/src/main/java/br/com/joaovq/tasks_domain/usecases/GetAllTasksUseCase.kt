package br.com.joaovq.tasks_domain.usecases

import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface GetAllTasksUseCase {
    operator fun invoke(): Flow<List<Task>>
}

class GetAllTasks @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetAllTasksUseCase {
    override fun invoke(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}
