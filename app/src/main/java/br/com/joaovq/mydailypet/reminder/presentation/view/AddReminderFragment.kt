package br.com.joaovq.mydailypet.reminder.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.format
import br.com.joaovq.mydailypet.core.util.extension.stringOrBlank
import br.com.joaovq.mydailypet.databinding.FragmentAddReminderBinding
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.AddReminderEvents
import br.com.joaovq.mydailypet.reminder.presentation.viewmodel.AddPetReminderViewModel
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderUiState
import br.com.joaovq.mydailypet.ui.adapter.SelectorPetsAdapter
import br.com.joaovq.mydailypet.ui.permission.NotificationPermissionManager
import br.com.joaovq.mydailypet.ui.util.extension.animateShrinkExtendedFabButton
import br.com.joaovq.mydailypet.ui.util.extension.createHelpDialog
import br.com.joaovq.mydailypet.ui.util.extension.goToSettingsAlertDialogForPermission
import br.com.joaovq.mydailypet.ui.util.extension.simpleBottomSheetDialog
import br.com.joaovq.mydailypet.ui.util.extension.simpleDatePickerDialog
import br.com.joaovq.mydailypet.ui.util.extension.simpleTimePicker
import br.com.joaovq.mydailypet.ui.util.extension.snackbar
import br.com.joaovq.mydailypet.ui.util.extension.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AddReminderFragment : Fragment() {
    private val binding by viewBinding(FragmentAddReminderBinding::inflate)
    private val addPetReminderViewModel: AddPetReminderViewModel by viewModels()
    private val args: AddReminderFragmentArgs by navArgs()
    private lateinit var calendar: Calendar
    private lateinit var notificationPermissionManager: NotificationPermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationPermissionManager = NotificationPermissionManager.from(this)
        notificationPermissionManager.setOnShowRationale {
            goToSettingsAlertDialogForPermission(
                message = R.string.message_alert_reminder_need_of_permission_notification,
            )
        }
    }

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
        setToolbarView()
        setListenersOfView()
        animateShrinkExtendedFabButton(fabButton = binding.fabAddReminder)
    }

    private fun initStates() {
        lifecycleScope.launch {
            addPetReminderViewModel.state.collectLatest { petState ->
                when (petState) {
                    is AddReminderUiState.Error -> {
                        snackbar(message = getString(petState.message))
                        binding.spSelectPetReminder.isEnabled = false
                    }

                    is AddReminderUiState.Success -> {
                        initSelectorPet(petState.data)
                        getArgs()
                    }

                    AddReminderUiState.SubmittedSuccess -> {
                        simpleBottomSheetDialog(text = getString(R.string.text_message_success_reminder_was_added))
                        notificationPermissionManager.checkPermission()
                        binding.etNameReminder.text?.clear()
                        binding.etDescriptionReminder.text?.clear()
                        setInitialView()
                    }

                    null -> {
                        setInitialView()
                    }
                }
            }
        }
        lifecycleScope.launch {
            addPetReminderViewModel.validateStateDate.collectLatest {
                binding.tilToDateReminder.error =
                    it.errorMessage.stringOrBlank(requireContext())
                binding.tilTimeReminder.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            addPetReminderViewModel.validateStateName.collectLatest {
                binding.tilNameReminder.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            addPetReminderViewModel.validateStateDescription.collectLatest {
                binding.tilDescriptionReminder.error =
                    it.errorMessage.stringOrBlank(requireContext())
            }
        }
    }

    private fun getArgs() {
        args.let {
            binding.etNameReminder.setText(it.name)
            binding.etDescriptionReminder.setText(it.description)
            it.pet?.let { petSafe ->
                binding.spSelectPetReminder.adapter =
                    SelectorPetsAdapter(requireContext(), listOf(petSafe))
                binding.spSelectPetReminder.isEnabled = false
            }
        }
    }

    private fun setInitialView() {
        calendar = Calendar.getInstance()
        binding.etToDateReminder.setText(calendar.time.format())
        binding.etTimeReminder.setText(
            adjustTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)),
        )
    }

    private fun setToolbarView() {
        binding.tbReminder.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tbReminder.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_help_reminder -> {
                    createHelpDialog(
                        icon = R.drawable.ic_help_outline,
                        message = R.string.message_help_add_reminder,
                    )
                }
            }
            true
        }
    }

    private fun setListenersOfView() {
        binding.fabAddReminder.setOnClickListener {
            binding.spSelectPetReminder.selectedItem?.let { selectedPet ->
                addPetReminderViewModel.dispatchIntent(
                    AddReminderEvents.SubmitData(
                        binding.etNameReminder.text.toString(),
                        binding.etDescriptionReminder.text.toString(),
                        calendar.time,
                        selectedPet as Pet,
                    ),
                )
            } ?: snackbar(message = getString(R.string.message_alert_add_reminder_not_found_pet))
        }
        binding.etToDateReminder.setOnClickListener {
            val calendarConstraintsBuilder = CalendarConstraints.Builder().setValidator(
                DateValidatorPointForward.now(),
            )
            simpleDatePickerDialog(
                title = getString(R.string.text_select_date),
                calendarConstraints = calendarConstraintsBuilder,
            ) { year, month, day ->
                calendar.set(year, month, day)
                binding.etToDateReminder.setText(calendar.time.format())
            }
        }
        binding.etTimeReminder.setOnClickListener {
            val timeFormat = when (binding.rgSelectTimeMode.checkedRadioButtonId) {
                R.id.rb_24_hrs -> TimeFormat.CLOCK_24H
                R.id.rb_am_pm -> TimeFormat.CLOCK_12H
                else -> TimeFormat.CLOCK_12H
            }
            simpleTimePicker(
                timeFormat = timeFormat,
            ) { hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                binding.etTimeReminder.setText(
                    adjustTime(hour, minute),
                )
            }
        }
    }

    private fun adjustTime(hour: Int, minute: Int): String {
        return if (minute < 10) {
            "$hour${DELIMITER_TIMER}0$minute"
        } else {
            "$hour$DELIMITER_TIMER$minute"
        }
    }

    private fun initSelectorPet(pets: List<Pet>) {
        binding.spSelectPetReminder.isEnabled = pets.isNotEmpty()
        binding.spSelectPetReminder.adapter =
            SelectorPetsAdapter(
                requireContext(),
                pets,
            )
    }

    companion object {
        const val DELIMITER_TIMER = ":"
    }
}
