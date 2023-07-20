package br.com.joaovq.mydailypet.home.presentation.adapter.task

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.joaovq.mydailypet.core.util.extension.format
import br.com.joaovq.mydailypet.databinding.ItemNotYetTaskPetListBinding
import br.com.joaovq.mydailypet.databinding.ItemTaskBinding
import br.com.joaovq.mydailypet.databinding.ItemTitleLayoutBinding
import br.com.joaovq.mydailypet.tasks.domain.model.Task

sealed class TaskListViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    data class TitleViewHolder(private val binding: ItemTitleLayoutBinding) :
        TaskListViewHolder(binding) {
        fun bind(@StringRes title: Int) {
            binding.tvTitleItem.setText(title)
        }
    }
    data class NotTaskViewHolder(private val binding: ItemNotYetTaskPetListBinding) :
        TaskListViewHolder(binding)

    data class TaskViewHolder(private val binding: ItemTaskBinding) :
        TaskListViewHolder(binding) {
        fun bind(task: Task) {
            binding.tvNameTask.text = task.name
            binding.tvCreatedAtTask.text = task.createdAt.format()
        }
    }
}
