package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import java.util.Calendar

object TestUtilReminder {
    private val toDateReminder = Calendar.getInstance().apply {
        add(Calendar.DATE, 3)
    }.time
    val reminder = Reminder(
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
