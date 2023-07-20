package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import java.util.Calendar
import java.util.Date

object TestUtil {
    private val notificationAlarmItem = NotificationAlarmItem(0L, "", "")
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

    private val toDateReminder: Date = Calendar.getInstance().apply {
        this.timeInMillis += 3000L
    }.time

    val reminder = Reminder(
        name = "Nina",
        description = "dsfjhfohgfud",
        alarmItem = NotificationAlarmItem(
            toDateReminder.time,
            "Nina",
            "dsfjhfohgfud",
        ),
        toDate = toDateReminder,
        pet = pet,
    )
}
