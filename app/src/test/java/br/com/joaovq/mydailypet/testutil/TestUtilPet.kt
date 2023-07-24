package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import java.util.Calendar
import java.util.Date

object TestUtilPet {
    private val birth: Date = Calendar.getInstance().time
    val petDto = PetDto(
        petId = 1,
        name = "Nina",
        breed = "Schnauz",
        animal = "dfs",
        weight = 0.34,
        birth = birth,
        birthAlarm = NotificationAlarmItem(
            birth.time,
            "Nina",
            "Schnauz",
        ),
    )
    val pet = Pet(
        id = 1,
        name = "Nina",
        breed = "Schnauz",
        animal = "dfs",
        weight = 0.34,
        birth = birth,
        birthAlarm = NotificationAlarmItem(
            birth.time,
            "Nina",
            "Schnauz",
        ),
    )

    val successfulValidateState = ValidateState(isValid = true)
    val errorValidateState = ValidateState(false)
}
