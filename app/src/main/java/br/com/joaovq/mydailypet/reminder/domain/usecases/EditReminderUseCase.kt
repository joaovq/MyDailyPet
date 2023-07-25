package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EditReminderUseCase {
    suspend operator fun invoke(reminder: Reminder)
}

class EditReminder @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : EditReminderUseCase {
    override suspend fun invoke(reminder: Reminder) {
        withContext(dispatcher) {
            reminderRepository.updateReminder(reminder)
        }
    }
}
