package br.com.joaovq.mydailypet.reminder.presentation.adapter.reminderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.joaovq.core.util.extension.compareSameDate
import br.com.joaovq.mydailypet.databinding.ItemMarkerDateReminderBinding
import br.com.joaovq.mydailypet.databinding.ItemReminderListBinding
import java.util.Calendar

class ReminderListAdapter(
    private val onClickReminder: (id: Int) -> Unit,
) :
    ListAdapter<ReminderListItem, ReminderListViewHolder>(DiffReminderList) {

    fun renderListReminder(reminders: List<br.com.joaovq.reminder_domain.model.Reminder>) {
        val items = mutableListOf<ReminderListItem>()
        val dates = reminders.map {
            it.toDate
        }.sortedDescending()
        val remindersNewList = mutableListOf<br.com.joaovq.reminder_domain.model.Reminder>()

        dates.forEach { date ->
            val calendar = Calendar.getInstance()
            val dateExistsInList = remindersNewList.any {
                calendar.time = it.toDate
                calendar.compareSameDate(date)
            }
            calendar.time = date
            if (!dateExistsInList) {
                items.add(ReminderListItem.MarkerDate(date))
                val remindersAtDate = reminders.filter {
                    calendar.compareSameDate(it.toDate)
                }
                remindersNewList.addAll(remindersAtDate)
                remindersAtDate.map { items.add(ReminderListItem.ReminderItem(it)) }
            }
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderListViewHolder {
        return when (viewType) {
            ReminderListItem.MARKER_DATE_VIEW_TYPE -> ReminderListViewHolder.MarkerDateViewHolder(
                ItemMarkerDateReminderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )

            ReminderListItem.REMINDER_VIEW_TYPE -> ReminderListViewHolder.ReminderItemListViewHolder(
                ItemReminderListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )

            else -> throw IllegalArgumentException("View type is illegal and not exist in view holder")
        }
    }

    override fun onBindViewHolder(holder: ReminderListViewHolder, position: Int) {
        when (holder) {
            is ReminderListViewHolder.MarkerDateViewHolder -> {
                holder.bind((getItem(position) as ReminderListItem.MarkerDate).date)
            }

            is ReminderListViewHolder.ReminderItemListViewHolder -> {
                holder.bind(
                    (getItem(position) as ReminderListItem.ReminderItem).reminder,
                    onClickReminder,
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReminderListItem.MarkerDate -> ReminderListItem.MARKER_DATE_VIEW_TYPE
            is ReminderListItem.ReminderItem -> ReminderListItem.REMINDER_VIEW_TYPE
        }
    }

    object DiffReminderList : DiffUtil.ItemCallback<ReminderListItem>() {
        override fun areItemsTheSame(
            oldItem: ReminderListItem,
            newItem: ReminderListItem,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ReminderListItem,
            newItem: ReminderListItem,
        ): Boolean {
            return when (oldItem) {
                is ReminderListItem.ReminderItem -> oldItem == (newItem as ReminderListItem.ReminderItem)
                is ReminderListItem.MarkerDate -> oldItem == (newItem as ReminderListItem.MarkerDate)
            }
        }
    }
}
