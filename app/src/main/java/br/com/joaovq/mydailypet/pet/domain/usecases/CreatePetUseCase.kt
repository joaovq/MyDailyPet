package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmInterval
import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface CreatePetUseCase {
    suspend operator fun invoke(pet: Pet)
}

class CreatePet @Inject constructor(
    private val petRepository: PetRepository,
    private val scheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CreatePetUseCase {
    override suspend fun invoke(pet: Pet) {
        withContext(dispatcher) {
            petRepository.insertPet(pet)
            scheduleBirthdayAlarm(pet.birthAlarm)
        }
    }

    private fun scheduleBirthdayAlarm(notificationAlarmItem: NotificationAlarmItem) {
        scheduler.scheduleRepeatAlarm(
            alarmItem = notificationAlarmItem,
            interval = TimeUnit.DAYS.toMillis(AlarmInterval.ONE_YEAR_IN_DAYS.value),
        )
    }
}
