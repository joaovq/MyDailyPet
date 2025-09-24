package br.com.joaovq.mydailypet.reminder.presentation.viewstate

import br.com.joaovq.core.util.intent.ValidateState
import br.com.joaovq.pet_domain.model.Pet

data class AddReminderState(
    val isLoading: Boolean = false,
    val pets: List<Pet> = emptyList(),
    val validateStateName: ValidateState = ValidateState(),
    val validateStateDescription: ValidateState = ValidateState(),
    val validateStateDate: ValidateState = ValidateState()
)
