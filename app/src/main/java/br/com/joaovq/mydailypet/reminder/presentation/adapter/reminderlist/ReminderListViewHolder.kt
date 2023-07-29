package br.com.joaovq.mydailypet.reminder.presentation.adapter.reminderlist

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.ItemMarkerDateReminderBinding
import br.com.joaovq.mydailypet.databinding.ItemReminderListBinding
import java.util.Calendar
import java.util.Date

sealed class ReminderListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    data class MarkerDateViewHolder(private val binding: ItemMarkerDateReminderBinding) :
        ReminderListViewHolder(binding) {
        fun bind(date: Date) {
            val calendarOfDate = Calendar.getInstance()
            calendarOfDate.time = date
            val actualCalendar = Calendar.getInstance()
            if (calendarOfDate.get(Calendar.DATE) == actualCalendar.get(Calendar.DATE)) {
                binding.tvMarkerDateReminder.setText(R.string.text_today)
            } else {
                binding.tvMarkerDateReminder.text = date.format()
            }
        }
    }

    data class ReminderItemListViewHolder(private val binding: ItemReminderListBinding) :
        ReminderListViewHolder(binding) {
        fun bind(
            reminderWithPet: br.com.joaovq.reminder_domain.model.Reminder,
            onClickReminderItem: (br.com.joaovq.reminder_domain.model.Reminder) -> Unit,
        ) {
            binding.tvNameReminderItemList.text = reminderWithPet.name
            binding.tvDescriptionReminderItemList.text = reminderWithPet.description
            val instance = Calendar.getInstance()
            instance.time = reminderWithPet.toDate
            val timeFormatted =
                "${instance.get(Calendar.HOUR_OF_DAY)}hrs${instance.get(Calendar.MINUTE)}min"
            binding.tvDateReminderItemList.text = timeFormatted
            binding.root.setOnClickListener {
                onClickReminderItem(reminderWithPet)
            }
        }
    }
}
