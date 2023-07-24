package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import java.util.Calendar
import java.util.Date

object TestUtilReminder {
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
        pet = TestUtilPet.pet,
    )
}
