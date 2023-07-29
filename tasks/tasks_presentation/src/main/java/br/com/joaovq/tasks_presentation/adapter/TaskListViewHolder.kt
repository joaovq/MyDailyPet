package br.com.joaovq.tasks_presentation.adapter

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_presentation.databinding.ItemCheckboxTaskBinding
import br.com.joaovq.tasks_presentation.databinding.ItemEditableTaskBinding

sealed class TaskListViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    data class CheckboxTaskViewHolder(private val binding: ItemCheckboxTaskBinding) :
        TaskListViewHolder(binding) {
        fun bind(
            task: Task,
            setOnCheckedChangeListener: (isChecked: Boolean, task: Task) -> Unit,
            setOnClickDeleteButton: (task: Task) -> Unit,
        ) {
            binding.cbTaskList.apply {
                isChecked = task.isCompleted
                paintFlags = paintStrikeLineText(task.isCompleted)
                text = task.name
                setOnCheckedChangeListener { _, isChecked ->
                    setOnCheckedChangeListener(isChecked, task)
                    paintFlags = paintStrikeLineText(isChecked)
                }
            }
            binding.ivDeleteTask.setOnClickListener {
                setOnClickDeleteButton(task)
            }
        }

        private fun paintStrikeLineText(isChecked: Boolean) = if (isChecked) {
            Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            Paint.LINEAR_TEXT_FLAG
        }
    }

    data class EditableViewHolder(private val binding: ItemEditableTaskBinding) :
        TaskListViewHolder(binding) {
        fun bind(setOnSendTask: (name: String) -> Unit) {
            binding.etTaskListName.setOnFocusChangeListener { _, isOnFocused ->
                binding.tilTaskListEditable.endIconDrawable?.setVisible(isOnFocused, false)
            }
            binding.tilTaskListEditable.setEndIconOnClickListener {
                setOnSendTask(binding.etTaskListName.text.toString())
                binding.etTaskListName.apply {
                    clearFocus()
                    text?.clear()
                }
            }
        }
    }
}
