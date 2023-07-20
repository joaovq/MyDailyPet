package br.com.joaovq.mydailypet.home.presentation.adapter.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.ItemNotYetTaskPetListBinding
import br.com.joaovq.mydailypet.databinding.ItemTaskBinding
import br.com.joaovq.mydailypet.databinding.ItemTitleLayoutBinding
import br.com.joaovq.mydailypet.tasks.domain.model.Task

class TaskListAdapter : ListAdapter<TaskListItem, TaskListViewHolder>(TaskListDiffUtil) {

    fun renderList(tasks: List<Task>) {
        val taskListItems = mutableListOf<TaskListItem>()
        when {
            tasks.isEmpty() -> taskListItems.add(TaskListItem.NotTaskAddedItem)
            else -> {
                taskListItems.add(TaskListItem.TitleItem(R.string.title_task))
                tasks.map { task: Task -> taskListItems.add(TaskListItem.TaskItem(task)) }
            }
        }
        submitList(taskListItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return when (viewType) {
            TaskListItem.TASK_ITEM_VIEW_TYPE -> {
                TaskListViewHolder.TaskViewHolder(
                    ItemTaskBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            TaskListItem.TITLE_ITEM_VIEW_TYPE -> {
                TaskListViewHolder.TitleViewHolder(
                    ItemTitleLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            TaskListItem.NOT_TASKS_ITEM_VIEW_TYPE -> {
                TaskListViewHolder.NotTaskViewHolder(
                    ItemNotYetTaskPetListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            else -> throw IllegalArgumentException("view type is not valid")
        }
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        when (holder) {
            is TaskListViewHolder.TaskViewHolder -> {
                holder.bind((getItem(position) as TaskListItem.TaskItem).task)
            }

            is TaskListViewHolder.TitleViewHolder -> {
                holder.bind((getItem(position) as TaskListItem.TitleItem).title)
            }

            is TaskListViewHolder.NotTaskViewHolder -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskListItem.TitleItem -> TaskListItem.TITLE_ITEM_VIEW_TYPE
            is TaskListItem.TaskItem -> TaskListItem.TASK_ITEM_VIEW_TYPE
            is TaskListItem.NotTaskAddedItem -> TaskListItem.NOT_TASKS_ITEM_VIEW_TYPE
        }
    }

    object TaskListDiffUtil : DiffUtil.ItemCallback<TaskListItem>() {
        override fun areItemsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return when (oldItem) {
                is TaskListItem.TaskItem -> oldItem == (newItem as TaskListItem.TaskItem)
                is TaskListItem.TitleItem -> oldItem == (newItem as TaskListItem.TitleItem)
                is TaskListItem.NotTaskAddedItem -> oldItem == (newItem as TaskListItem.NotTaskAddedItem)
            }
        }
    }
}
