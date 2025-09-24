package br.com.joaovq.mydailypet.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.mydailypet.databinding.ItemReminderBinding
import br.com.joaovq.reminder_domain.model.Reminder
import java.util.Calendar

class RemindersAdapter(
    private val items: List<Reminder>,
    private val onClickReminder: (id: Int) -> Unit,
) : RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>() {

    class RemindersViewHolder(
        private val binding: ItemReminderBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder, onClickReminder: (id: Int) -> Unit) {
            binding.tvNameItemReminder.text = reminder.name
            binding.tvDescriptionItemReminder.text = reminder.description
            val calendar = Calendar.getInstance()
            calendar.time = reminder.toDate
            val dateTimeFormat = getDateTimeFormat(calendar)
            binding.tvFromDateItemReminder.text = dateTimeFormat
            binding.root.setOnClickListener {
                onClickReminder(reminder.id)
            }
        }
        fun getDateTimeFormat(
            calendar: Calendar
        ): String = "${calendar.time.format()} - ${calendar[Calendar.HOUR_OF_DAY]}:${
            calendar[Calendar.MINUTE]
        }"
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
