package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.reminder_domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeleteAllRemindersUseCase {
    suspend operator fun invoke()
}

class DeleteAllReminders @Inject constructor(
    private val reminderRepository: ReminderRepository,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : DeleteAllRemindersUseCase {
    override suspend fun invoke() {
        withContext(dispatcher) {
            reminderRepository.deleteAll()
        }
    }
}
