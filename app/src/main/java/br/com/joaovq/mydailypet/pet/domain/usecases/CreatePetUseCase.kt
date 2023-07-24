package br.com.joaovq.mydailypet.pet.domain.usecases

import android.graphics.Bitmap
import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmInterval
import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface CreatePetUseCase {
    suspend operator fun invoke(pet: Pet, bitmap: Bitmap?)
}

class CreatePet @Inject constructor(
    private val petRepository: PetRepository,
    private val saveImagePetUseCase: SaveImagePetUseCase,
    private val scheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CreatePetUseCase {
    override suspend fun invoke(pet: Pet, bitmap: Bitmap?) {
        withContext(dispatcher) {
            val newPet = if (pet.imageUrl.isNotBlank() && bitmap != null) {
                val pathImageSaved = saveImagePetUseCase.invoke(
                    bitmap,
                    pet.imageUrl,
                )
                pet.copy(imageUrl = pathImageSaved ?: "")
            } else {
                pet
            }
            petRepository.insertPet(newPet)
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
