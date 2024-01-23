package br.com.joaovq.mydailypet.reminder.presentation.adapter.suggestedReminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.mydailypet.databinding.ItemSuggestedReminderBinding

class SuggestedReminderAdapter(
    private val onClickItem: (View, SuggestedReminderItem) -> Unit = { _, _ -> }
) :
    ListAdapter<SuggestedReminderItem, SuggestedReminderAdapter.SuggestedReminderViewHolder>(
        SuggestedReminderDiffUtilItemCallback
    ) {

    inner class SuggestedReminderViewHolder(private val binding: ItemSuggestedReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: SuggestedReminderItem) {
            binding.text = reminder.name
            binding.ivSuggestedReminder.setImageResource(reminder.icon)
            binding.root.setOnClickListener {
                onClickItem(it, reminder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedReminderViewHolder {
        val binding = ItemSuggestedReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SuggestedReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestedReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object SuggestedReminderDiffUtilItemCallback : DiffUtil.ItemCallback<SuggestedReminderItem>() {
        override fun areItemsTheSame(
            oldItem: SuggestedReminderItem,
            newItem: SuggestedReminderItem
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SuggestedReminderItem,
            newItem: SuggestedReminderItem
        ): Boolean =
            when (oldItem.name) {
                newItem.name -> true
                else -> false
            }

    }
}


data class SuggestedReminderItem(
    val name: String,
    @DrawableRes val icon: Int
)