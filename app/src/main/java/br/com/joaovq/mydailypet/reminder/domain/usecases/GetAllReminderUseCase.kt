package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.mappers.toReminder
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetAllReminderUseCase {
    suspend operator fun invoke(): Flow<List<Reminder>>
}

class GetAllReminders @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetAllReminderUseCase {
    override suspend fun invoke(): Flow<List<Reminder>> {
        return withContext(dispatcher) {
            reminderRepository.getAllReminders()
                .map { reminders -> reminders.map { it.toReminder() } }
        }
    }
}
