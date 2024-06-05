package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.util.intent.ValidateState
import br.com.joaovq.model.PetDto
import br.com.joaovq.pet_domain.model.Pet
import java.util.Calendar
import java.util.Date
import java.util.UUID

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
            "Schnauzer",
            id = UUID.randomUUID()
        ),
    )
    val pet = Pet(
        id = 1,
        name = "Nina",
        breed = "Schnauzer",
        animal = "dfs",
        weight = 0.34,
        birth = birth,
        birthAlarm = NotificationAlarmItem(
            birth.time,
            "Nina",
            "Schnauzer",
            id = UUID.randomUUID()
        ),
    )

    val successfulValidateState = ValidateState(isValid = true)
    val errorValidateState = ValidateState(false)
}
