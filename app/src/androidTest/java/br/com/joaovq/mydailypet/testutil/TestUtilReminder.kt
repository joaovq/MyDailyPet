package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.core.model.NotificationAlarmItem
import java.util.Calendar

object TestUtilReminder {
    private val toDateReminder = Calendar.getInstance().apply {
        add(Calendar.DATE, 3)
    }.time
    val reminder = br.com.joaovq.reminder_domain.model.Reminder(
        id = 1,
        name = "Put meet for nina",
        description = "My description test",
        pet = TestUtilPet.pet,
        toDate = toDateReminder,
        alarmItem = NotificationAlarmItem(
            toDateReminder.time,
            "Put meet for nina",
            "My description test",
        ),
    )
}
