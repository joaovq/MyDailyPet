package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetAllReminderUseCase {
    suspend operator fun invoke(): Flow<List<Reminder>>
}

class GetAllReminders @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetAllReminderUseCase {
    override suspend fun invoke(): Flow<List<Reminder>> {
        return withContext(dispatcher) {
            reminderRepository.getAllReminders()
        }
    }
}
