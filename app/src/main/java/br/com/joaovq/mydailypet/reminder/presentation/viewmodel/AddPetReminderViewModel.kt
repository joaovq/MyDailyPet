package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.AddReminderIntents
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderEvents
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderState
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.usecases.GetAllPetsUseCase
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.usecases.CreateReminderUseCase
import br.com.joaovq.reminder_domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.reminder_domain.usecases.ValidateFieldTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
) : ViewModel() {
    private val _event: MutableSharedFlow<AddReminderEvents> =
        MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(AddReminderState())
    val state = getAllPetsUseCase().combine(_state) { pets, state ->
        state.copy(pets = pets)
    }.flowOn(dispatcher)
        .catch { throwable ->
            _event.emit(AddReminderEvents.Error(exception = throwable))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            AddReminderState()
        )


    fun dispatchIntent(intent: AddReminderIntents) {
        when (intent) {
            is AddReminderIntents.SubmitData -> submitData(
                intent.name,
                intent.description,
                intent.toDate,
                intent.pet,
            )
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
                _state.update { it.copy(validateStateName = validateFieldTextUseCase(name)) }
                _state.update {
                    it.copy(
                        validateStateDescription = validateFieldTextUseCase(
                            description
                        )
                    )
                }
                _state.update { it.copy(validateStateDate = validateDateTimeReminder(toDate)) }

                val states = listOf(
                    _state.value.validateStateDate,
                    _state.value.validateStateName,
                    _state.value.validateStateDescription,
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
                    _event.emit(AddReminderEvents.SubmittedSuccess)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _event.emit(
                    AddReminderEvents.Error(
                        exception = e,
                    )
                )
            }
        }
    }
}
