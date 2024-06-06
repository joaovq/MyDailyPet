package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.core.model.NotificationAlarmItem
import java.util.Calendar
import java.util.Date
import java.util.UUID

object TestUtilReminder {
    private val toDateReminder: Date = Calendar.getInstance().apply {
        this.timeInMillis += 3000L
    }.time
    val reminder = br.com.joaovq.reminder_domain.model.Reminder(
        name = "Nina",
        description = "dsfjhfohgfud",
        alarmItem = NotificationAlarmItem(
            toDateReminder.time,
            "Nina",
            "dsfjhfohgfud",
            id = UUID.randomUUID()
        ),
        toDate = toDateReminder,
        pet = TestUtilPet.pet,
    )
}
