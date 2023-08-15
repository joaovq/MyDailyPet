package br.com.joaovq.mydailypet.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.mydailypet.databinding.ItemReminderBinding
import br.com.joaovq.reminder_domain.model.Reminder

class RemindersAdapter(
    private val items: List<Reminder>,
    private val onClickReminder: (id:Int) -> Unit,
) : RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>() {

    class RemindersViewHolder(
        private val binding: ItemReminderBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder, onClickReminder: (id:Int) -> Unit) {
            binding.tvNameItemReminder.text = reminder.name
            binding.tvDescriptionItemReminder.text = reminder.description
            binding.tvFromDateItemReminder.text = reminder.toDate.format()
            binding.root.setOnClickListener {
                onClickReminder(reminder.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        val binding =
            ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RemindersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        holder.bind(items[position], onClickReminder)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
