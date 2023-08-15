package br.com.joaovq.mydailypet.reminder.presentation.view

import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.core.util.extension.stringOrBlank
import br.com.joaovq.core_ui.extension.createHelpDialog
import br.com.joaovq.core_ui.extension.hideKeyboard
import br.com.joaovq.core_ui.extension.loadImage
import br.com.joaovq.core_ui.extension.navWithAnim
import br.com.joaovq.core_ui.extension.simpleAlertDialog
import br.com.joaovq.core_ui.extension.toast
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentReminderBinding
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.ReminderIntent
import br.com.joaovq.mydailypet.reminder.presentation.viewmodel.ReminderViewModel
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.ReminderState
import br.com.joaovq.reminder_domain.model.Reminder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class ReminderFragment : Fragment() {
    private val binding: FragmentReminderBinding by viewBinding(FragmentReminderBinding::inflate)
    private val args by navArgs<ReminderFragmentArgs>()
    private var actionMode: ActionMode? = null
    private val reminderViewModel: ReminderViewModel by viewModels()
    private var reminder: Reminder? = null

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
        setupView()
        setupToolbar()
    }

    private fun initStates() {
        lifecycleScope.launch {
            reminderViewModel.validName.collectLatest {
                binding.tilNameReminder.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            reminderViewModel.validDescription.collectLatest {
                binding.tilDescriptionEditReminder.error =
                    it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            reminderViewModel.state.collectLatest { value: ReminderState? ->
                value?.let { safeState ->
                    when (safeState) {
                        is ReminderState.Error -> {
                            safeState.exception.message?.let {
                                toast(
                                    text = it,
                                )
                            }
                        }

                        is ReminderState.Success -> {
                            safeState.reminder?.let { safeReminder ->
                                reminder = safeReminder
                                with(binding) {
                                    reminder = safeReminder
                                    civPetImageReminder.loadImage(safeReminder.pet.imageUrl)
                                    val calendar = Calendar.getInstance()
                                    calendar.time = safeReminder.toDate
                                    val dateTimeFormat = getDateTimeFormat(calendar)
                                    tvFromDateReminder.text = dateTimeFormat
                                }
                            }
                        }

                        ReminderState.SuccessDelete -> {
                            toast(text = getString(br.com.joaovq.core_ui.R.string.message_success))
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        if (args.idReminder != 0) {
            reminderViewModel.dispatchIntent(
                ReminderIntent.GetReminder(
                    args.idReminder,
                ),
            )
        }
    }

    private fun getDateTimeFormat(calendar: Calendar) =
        "${calendar.time.format()} - ${calendar.get(Calendar.HOUR_OF_DAY)}:${
            calendar.get(Calendar.MINUTE)
        }"

    private fun setupToolbar() {
        binding.tbReminderFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvNameReminder.setOnFocusChangeListener { _, b ->
            setActionModeOnFocusEditText(b)
        }
        binding.tvDescriptionReminder.setOnFocusChangeListener { _, b ->
            setActionModeOnFocusEditText(b)
        }
        binding.tbReminderFragment.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item__menu_help_detail_reminder -> {
                    createHelpDialog(message = R.string.message_help_detail_reminder)
                    true
                }

                R.id.item_menu_repeat_detail_reminder -> {
                    reminder?.let {
                        findNavController().navWithAnim(
                            ReminderFragmentDirections.actionReminderFragmentToAddReminderFragment(
                                it.name,
                                it.description,
                            ),
                        )
                    }
                    true
                }

                R.id.item_menu_delete_detail_reminder -> {
                    simpleAlertDialog(
                        message = R.string.message_alert_delete_reminder,
                    ) {
                        deleteReminder()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setActionModeOnFocusEditText(isFocused: Boolean) {
        if (isFocused) {
            actionMode = binding.tbReminderFragment.startActionMode(
                callback,
            )
            actionMode?.title = getString(R.string.title_action_mode_edit_reminder)
        } else {
            binding.tilNameReminder.clearFocus()
            binding.tvDescriptionReminder.clearFocus()
        }
    }

    val callback = object : ActionMode.Callback {
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            val menuInflater = MenuInflater(requireContext())
            menuInflater.inflate(
                R.menu.menu_action_edit_reminder,
                p1,
            )
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(actionMode: ActionMode?, p1: MenuItem?): Boolean {
            return when (p1?.itemId) {
                R.id.item_complete_edit_reminder -> {
                    updateReminder()
                    hideKeyBoard()
                    actionMode?.finish()
                    true
                }

                else -> {
                    setupView()
                    false
                }
            }
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
        }
    }

    private fun deleteReminder() {
        reminder?.let {
            reminderViewModel.dispatchIntent(
                ReminderIntent.DeleteReminder(
                    args.idReminder,
                    reminder = it,
                ),
            )
        }
    }

    private fun updateReminder() {
        reminder?.let {
            reminderViewModel.dispatchIntent(
                ReminderIntent.EditReminder(
                    args.idReminder,
                    Reminder(
                        name = binding.tvNameReminder.text.toString(),
                        description = binding.tvDescriptionReminder.text.toString(),
                        pet = it.pet,
                        toDate = it.toDate,
                        alarmItem = it.alarmItem,
                    ),
                ),
            )
        }
    }

    private fun hideKeyBoard() {
        val activity = requireActivity() as AppCompatActivity
        activity.hideKeyboard()
    }
}
