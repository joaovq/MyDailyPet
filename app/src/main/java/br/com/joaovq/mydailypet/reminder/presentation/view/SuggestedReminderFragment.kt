package br.com.joaovq.mydailypet.reminder.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentSuggestedReminderBinding
import br.com.joaovq.mydailypet.reminder.presentation.adapter.suggestedReminder.SuggestedReminderAdapter
import br.com.joaovq.mydailypet.reminder.presentation.adapter.suggestedReminder.SuggestedReminderItem
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.reminder_domain.model.Reminder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SuggestedReminderFragment : Fragment() {
    private val binding by viewBinding(FragmentSuggestedReminderBinding::inflate)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suggestedReminderAdapter = SuggestedReminderAdapter { _, reminder ->
            findNavController().navigate(
                SuggestedReminderFragmentDirections.actionSuggestedReminderFragmentToAddReminderFragment(
                    reminder.name,
                    null
                )
            )
        }
        binding.btnAddSuggestedReminder.setOnClickListener {
            findNavController().navigate(
                SuggestedReminderFragmentDirections.actionSuggestedReminderFragmentToAddReminderFragment(
                )
            )
        }
        binding.tbSuggestedReminder.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvSuggestedReminders.adapter = suggestedReminderAdapter
        suggestedReminderAdapter.submitList(
            listOf(
                SuggestedReminderItem(
                    getString(R.string.suggested_reminder_pet_shop),
                    R.drawable.ic_shop
                ),
                SuggestedReminderItem(
                    getString(R.string.suggested_reminder_wash),
                    R.drawable.ic_clean_dry
                ),
                SuggestedReminderItem(
                    getString(R.string.suggested_reminder_walk),
                    R.drawable.ic_dog
                ),
                SuggestedReminderItem(
                    getString(R.string.suggested_reminder_medicament),
                    R.drawable.ic_pill
                ),
            )
        )
    }
}