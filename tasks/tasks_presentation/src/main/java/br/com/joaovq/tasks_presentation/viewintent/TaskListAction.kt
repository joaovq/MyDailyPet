package br.com.joaovq.tasks_presentation.viewintent

import br.com.joaovq.tasks_domain.model.Task

sealed interface TaskListAction {
    data class CreateTask(
        val task: Task,
    ) : TaskListAction

    object GetAllTasks : TaskListAction
    data class DeleteTask(
        val id: Int,
        val task: Task,
    ) : TaskListAction

    data class UpdateStatusCompletedTask(
        val id: Int,
        val task: Task,
        val isCompletedTask: Boolean,
    ) : TaskListAction
}
