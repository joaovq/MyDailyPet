package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.reminder_domain.model.Reminder
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetReminderUseCase {
    suspend operator fun invoke(id: Int): Flow<Reminder>
}

class GetReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : GetReminderUseCase {
    override suspend fun invoke(id: Int): Flow<Reminder> {
        return withContext(dispatcher) {
            reminderRepository.getReminderById(id)
        }
    }
}
