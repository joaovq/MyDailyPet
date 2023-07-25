package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeleteAllRemindersUseCase {
    suspend operator fun invoke()
}

class DeleteAllReminders @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : DeleteAllRemindersUseCase {
    override suspend fun invoke() {
        withContext(dispatcher) {
            reminderRepository.deleteAll()
        }
    }
}
