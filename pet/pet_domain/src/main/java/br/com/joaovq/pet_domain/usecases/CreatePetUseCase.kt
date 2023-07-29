package br.com.joaovq.pet_domain.usecases

import android.graphics.Bitmap
import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.data.alarm.AlarmInterval
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
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
