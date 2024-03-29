package br.com.joaovq.tasks_presentation.adapter

import br.com.joaovq.tasks_domain.model.Task

sealed interface TaskListItem {
    data class CheckboxTaskItem(val task: Task) : TaskListItem
    object EditableAddTask : TaskListItem

    companion object {
        const val CHECKBOX_TASK_ITEM_VIEW_TYPE = 0
        const val EDITABLE_ITEM_VIEW_TYPE = 1
    }
}
