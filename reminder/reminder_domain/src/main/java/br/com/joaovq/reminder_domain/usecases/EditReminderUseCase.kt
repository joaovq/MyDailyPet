package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.reminder_domain.repository.ReminderRepository
import br.com.joaovq.reminder_domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EditReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class EditReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : EditReminderUseCase {
    override suspend fun invoke(reminder: Reminder) {
        withContext(dispatcher) {
            reminderRepository.updateReminder(reminder)
        }
    }
}
