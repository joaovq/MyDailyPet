package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.util.intent.ValidateState
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.ReminderIntent
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.ReminderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val editReminderUseCase: br.com.joaovq.reminder_domain.usecases.EditReminderUseCase,
    private val validateFieldText: br.com.joaovq.reminder_domain.usecases.ValidateFieldText,
    private val deleteReminderUseCase: br.com.joaovq.reminder_domain.usecases.DeleteReminderUseCase,
    @br.com.joaovq.core.di.IODispatcher private val dispatcher: CoroutineDispatcher,
) : br.com.joaovq.core_ui.presenter.BaseViewModel<ReminderIntent, ReminderState?>() {
    override val _state: MutableStateFlow<ReminderState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
    private val _validName: MutableStateFlow<ValidateState> = MutableStateFlow(
        ValidateState(),
    )
    val validName = _validName.asStateFlow()
    private val _validDescription: MutableStateFlow<ValidateState> =
        MutableStateFlow(ValidateState())
    val validDescription = _validDescription.asStateFlow()

    override fun dispatchIntent(intent: ReminderIntent) {
        when (intent) {
            is ReminderIntent.EditReminder -> {
                updateReminder(intent.id, intent.reminder)
            }

            is ReminderIntent.DeleteReminder -> deleteReminder(intent.id, intent.reminder)
        }
    }

    private fun updateReminder(id: Int, reminder: br.com.joaovq.reminder_domain.model.Reminder) {
        viewModelScope.launch(dispatcher) {
            val stateName = validateFieldText(reminder.name)
            val stateDescription = validateFieldText(reminder.description)
            _validName.value = stateName
            _validDescription.value = stateDescription
            val stateList = listOf(
                stateName,
                stateDescription,
            )
            if (validateForms(stateList)) {
                try {
                    editReminderUseCase(
                        reminder.copy(id = id),
                    )
                    _state.value = ReminderState.Success(
                        br.com.joaovq.core_ui.R.string.message_success,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    _state.value = ReminderState.Success(
                        br.com.joaovq.core_ui.R.string.message_error,
                    )
                }
            }
        }
    }

    private fun deleteReminder(id: Int, reminder: br.com.joaovq.reminder_domain.model.Reminder) {
        viewModelScope.launch(dispatcher) {
            try {
                deleteReminderUseCase(reminder.copy(id = id))
                _state.value = ReminderState.SuccessDelete
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ReminderState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun validateForms(states: List<ValidateState>) = states.all { it.isValid }
}
