package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeletePetUseCase {
    suspend operator fun invoke(pet: Pet)
}

class DeletePet @Inject constructor(
    private val petRepository: PetRepository,
    private val scheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : DeletePetUseCase {
    override suspend fun invoke(pet: Pet) {
        withContext(dispatcher) {
            petRepository.deletePet(pet)
            cancelBirthdayAlarm(pet.birthAlarm)
        }
    }

    private fun cancelBirthdayAlarm(notificationAlarmItem: NotificationAlarmItem) {
        scheduler.cancel(notificationAlarmItem)
    }
}
