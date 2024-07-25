package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.util.intent.ValidateState
import br.com.joaovq.core_ui.presenter.BaseViewModel
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.AddReminderEvents
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderUiState
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.usecases.GetAllPetsUseCase
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.usecases.CreateReminderUseCase
import br.com.joaovq.reminder_domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.reminder_domain.usecases.ValidateFieldTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddPetReminderViewModel @Inject constructor(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val createReminderUseCase: CreateReminderUseCase,
    private val validateDateTimeReminder: ValidateDateTimeReminderUseCase,
    private val validateFieldTextUseCase: ValidateFieldTextUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<AddReminderEvents, AddReminderUiState<List<Pet>>?>() {
    override val _state: MutableStateFlow<AddReminderUiState<List<Pet>>?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    private val _validateStateDate = MutableStateFlow(ValidateState())
    val validateStateDate = _validateStateDate.asStateFlow()

    private val _validateStateName = MutableStateFlow(ValidateState())
    val validateStateName = _validateStateName.asStateFlow()

    private val _validateStateDescription = MutableStateFlow(ValidateState())
    val validateStateDescription = _validateStateDescription.asStateFlow()

    init {
        getAllPets()
    }

    override fun dispatchIntent(intent: AddReminderEvents) {
        when (intent) {
            AddReminderEvents.GetAllPets -> getAllPets()
            is AddReminderEvents.SubmitData -> submitData(
                intent.name,
                intent.description,
                intent.toDate,
                intent.pet,
            )
        }
    }

    private fun getAllPets() {
        viewModelScope.launch(dispatcher) {
            try {
                getAllPetsUseCase().collectLatest {
                    _state.value = AddReminderUiState.Success(
                        it,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = AddReminderUiState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun submitData(
        name: String,
        description: String,
        toDate: Date?,
        pet: Pet,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                _validateStateName.value = validateFieldTextUseCase(name)
                _validateStateDescription.value = validateFieldTextUseCase(description)
                _validateStateDate.value = validateDateTimeReminder(date = toDate)

                val states = listOf(
                    _validateStateDate.value,
                    _validateStateName.value,
                    _validateStateDescription.value,
                )
                if (states.all { it.isValid } && toDate != null) {
                    val notificationAlarm = NotificationAlarmItem(
                        toDate.time - Date().time,
                        name,
                        description,
                        id = UUID.randomUUID()
                    )
                    createReminderUseCase(
                        Reminder(
                            name = name,
                            description = description,
                            toDate = toDate,
                            pet = pet,
                            alarmItem = notificationAlarm,
                        ),
                    )
                    _state.value = AddReminderUiState.SubmittedSuccess
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = AddReminderUiState.Error(
                    exception = e,
                )
            }
        }
    }
}
