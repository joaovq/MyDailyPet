package br.com.joaovq.mydailypet.reminder.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.core_ui.extension.simpleAlertDialog
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentReminderListBinding
import br.com.joaovq.mydailypet.reminder.presentation.adapter.reminderlist.ReminderListAdapter
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.ReminderListIntent
import br.com.joaovq.mydailypet.reminder.presentation.viewmodel.ReminderListViewModel
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.RemindersListState
import br.com.joaovq.reminder_domain.model.Reminder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class ReminderListFragment : Fragment() {
    private val binding: FragmentReminderListBinding by viewBinding(FragmentReminderListBinding::inflate)
    private val reminderListViewModel by viewModels<ReminderListViewModel>()
    private var remindersListAdapter: ReminderListAdapter? = null
    private var reminders: List<Reminder>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStates()
        initToolbar()
    }

    private fun initStates() {
        lifecycleScope.launch {
            reminderListViewModel.state.collectLatest { state ->
                state?.let { safeState ->
                    when (safeState) {
                        is RemindersListState.Success -> {
                            binding.rvRemindersList.setHasFixedSize(true)
                            binding.rvRemindersList.adapter =
                                ReminderListAdapter { idReminder ->
                                    findNavController().navigate(
                                        ReminderListFragmentDirections.actionReminderListFragmentToReminderFragment(
                                            idReminder,
                                        ),
                                    )
                                }.also {
                                    it.renderListReminder(safeState.reminders)
                                    reminders = safeState.reminders
                                    remindersListAdapter = it
                                }
                            binding.tbRemindersList.menu[0].isVisible =
                                reminders?.isNotEmpty() == true
                        }

                        is RemindersListState.Error -> {
                            safeState.exception.message?.let { snackbar(message = it) }
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        binding.tbRemindersList.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tbRemindersList.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_all_reminders_menu -> {
                    reminders?.let {
                        remindersListAdapter?.renderListReminder(
                            it,
                        )
                    }
                    true
                }

                R.id.item_today_menu_filter -> {
                    filterTodayReminders()?.let {
                        remindersListAdapter?.renderListReminder(
                            it,
                        )
                    }
                    true
                }

                R.id.item_delete_all_reminders_list -> {
                    simpleAlertDialog(
                        message = R.string.message_alert_delete_all_reminders,
                    ) {
                        reminderListViewModel.dispatchIntent(ReminderListIntent.DeleteAllReminders)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun filterTodayReminders() = reminders?.filter {
        val calendarNow = Calendar.getInstance()
        val calendarDate = Calendar.getInstance()
        calendarDate.time = it.toDate
        calendarNow.get(Calendar.DATE) == calendarDate.get(Calendar.DATE)
    }
}
