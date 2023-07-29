package br.com.joaovq.mydailypet.reminder.presentation.viewintent

import java.util.Date

sealed interface AddReminderEvents {
    object GetAllPets : AddReminderEvents
    data class SubmitData(
        val name: String,
        val description: String,
        val toDate: Date?,
        val pet: br.com.joaovq.pet_domain.model.Pet,
    ) : AddReminderEvents
}
