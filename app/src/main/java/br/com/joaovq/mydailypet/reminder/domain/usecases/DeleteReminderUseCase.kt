package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeleteReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class DeleteReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val scheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
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
