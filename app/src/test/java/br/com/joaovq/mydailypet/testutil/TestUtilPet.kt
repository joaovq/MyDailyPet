package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.util.intent.ValidateState
import java.util.Calendar
import java.util.Date

object TestUtilPet {
    private val birth: Date = Calendar.getInstance().time
    val petDto = br.com.joaovq.model.PetDto(
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
    val pet = br.com.joaovq.pet_domain.model.Pet(
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
