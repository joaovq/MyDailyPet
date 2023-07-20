package br.com.joaovq.mydailypet.home.presentation.adapter.task

import androidx.annotation.StringRes
import br.com.joaovq.mydailypet.tasks.domain.model.Task

sealed interface TaskListItem {
    data class TitleItem(@StringRes val title: Int) : TaskListItem
    data class TaskItem(val task: Task) : TaskListItem
    object NotTaskAddedItem : TaskListItem

    companion object {
        const val TASK_ITEM_VIEW_TYPE = 0
        const val TITLE_ITEM_VIEW_TYPE = 1
        const val NOT_TASKS_ITEM_VIEW_TYPE = 2
    }
}
