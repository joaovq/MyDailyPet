package br.com.joaovq.mydailypet.tasks.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.joaovq.mydailypet.databinding.ItemCheckboxTaskBinding
import br.com.joaovq.mydailypet.databinding.ItemEditableTaskBinding
import br.com.joaovq.mydailypet.tasks.domain.model.Task

class TaskListAdapter(
    private val taskListOnClickListener: TaskListOnClickListener,
) : ListAdapter<TaskListItem, TaskListViewHolder>(DiffUtilTaskListItem) {

    fun renderTaskList(tasks: List<Task>) {
        val listItems = mutableListOf<TaskListItem>()

        when {
            tasks.isEmpty() -> listItems.add(TaskListItem.EditableAddTask)
            else -> {
                tasks.map { listItems.add(TaskListItem.CheckboxTaskItem(it)) }
                listItems.add(TaskListItem.EditableAddTask)
            }
        }

        submitList(listItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return when (viewType) {
            TaskListItem.CHECKBOX_TASK_ITEM_VIEW_TYPE -> {
                TaskListViewHolder.CheckboxTaskViewHolder(
                    ItemCheckboxTaskBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            TaskListItem.EDITABLE_ITEM_VIEW_TYPE -> {
                TaskListViewHolder.EditableViewHolder(
                    ItemEditableTaskBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }

            else -> throw IllegalArgumentException("Illegal view type given")
        }
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        when (holder) {
            is TaskListViewHolder.CheckboxTaskViewHolder -> {
                val checkboxTaskItem = getItem(position) as TaskListItem.CheckboxTaskItem
                taskListOnClickListener.apply {
                    holder.bind(
                        checkboxTaskItem.task,
                        ::setOnCheckTask,
                        ::setOnClickDeleteButton,
                    )
                }
            }

            is TaskListViewHolder.EditableViewHolder -> {
                holder.bind(taskListOnClickListener::setCompletedCreatedTask)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskListItem.CheckboxTaskItem -> TaskListItem.CHECKBOX_TASK_ITEM_VIEW_TYPE
            TaskListItem.EditableAddTask -> TaskListItem.EDITABLE_ITEM_VIEW_TYPE
        }
    }

    object DiffUtilTaskListItem : DiffUtil.ItemCallback<TaskListItem>() {
        override fun areItemsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskListItem, newItem: TaskListItem): Boolean {
            return when (oldItem) {
                is TaskListItem.CheckboxTaskItem -> {
                    oldItem.task == (newItem as TaskListItem.CheckboxTaskItem).task
                }

                TaskListItem.EditableAddTask -> {
                    oldItem == (newItem as TaskListItem.EditableAddTask)
                }
            }
        }
    }

    interface TaskListOnClickListener {
        fun setOnCheckTask(isChecked: Boolean, task: Task)
        fun setCompletedCreatedTask(name: String)
        fun setOnClickDeleteButton(task: Task)
    }
}
