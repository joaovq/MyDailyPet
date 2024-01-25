package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.core.di.AlarmManager
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DeletePetUseCase {
    suspend operator fun invoke(pet: Pet)
}

class DeletePet @Inject constructor(
    private val petRepository: PetRepository,
    @AlarmManager private val scheduler: AlarmScheduler,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
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
