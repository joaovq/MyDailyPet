package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CreateReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class CreateReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CreateReminderUseCase {
    override suspend fun invoke(reminder: Reminder) {
        withContext(dispatcher) {
            reminderRepository.insertReminder(reminder)
            scheduleOneTimeAlarm(reminder.alarmItem)
        }
    }

    private fun scheduleOneTimeAlarm(notificationAlarmItem: NotificationAlarmItem) {
        alarmScheduler.scheduleExactAlarmAllowWhileIdle(
            alarmItem = notificationAlarmItem,
        )
    }
}
