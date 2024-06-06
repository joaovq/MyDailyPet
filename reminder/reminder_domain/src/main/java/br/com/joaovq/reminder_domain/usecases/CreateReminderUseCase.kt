package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.core.di.SchedulerManager
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CreateReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class CreateReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @SchedulerManager private val alarmScheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CreateReminderUseCase {
    override suspend fun invoke(reminder: Reminder) {
        withContext(dispatcher) {
            val id = reminderRepository.insertReminder(reminder)
            scheduleOneTimeAlarm(reminder.alarmItem.copy(reminderId = id))
        }
    }

    private fun scheduleOneTimeAlarm(notificationAlarmItem: NotificationAlarmItem) {
        alarmScheduler.scheduleExactAlarmAllowWhileIdle(
            alarmItem = notificationAlarmItem,
        )
    }
}
