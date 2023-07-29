package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeleteReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class DeleteReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val scheduler: AlarmScheduler,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : DeleteReminderUseCase {
    override suspend fun invoke(reminder: Reminder) {
        withContext(dispatcher) {
            reminderRepository.deleteReminder(reminder)
            cancelReminderAlarm(reminder.alarmItem)
        }
    }

    private fun cancelReminderAlarm(notificationAlarmItem: NotificationAlarmItem) {
        scheduler.cancel(notificationAlarmItem)
    }
}
