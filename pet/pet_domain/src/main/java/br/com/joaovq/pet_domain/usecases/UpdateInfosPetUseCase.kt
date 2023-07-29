package br.com.joaovq.pet_domain.usecases

import android.graphics.Bitmap
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.data.alarm.AlarmInterval
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface UpdateInfosPetUseCase {
    suspend operator fun invoke(pet: Pet, bitmap: Bitmap?)
}

class UpdateInfosPet @Inject constructor(
    private val repository: PetRepository,
    private val saveImagePetUseCase: SaveImagePetUseCase,
    private val alarmScheduler: AlarmScheduler,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : UpdateInfosPetUseCase {
    override suspend fun invoke(pet: Pet, bitmap: Bitmap?) {
        withContext(dispatcher) {
            val isUpdatedPhoto = pet.imageUrl.isNotBlank()
            val newPet = if (isUpdatedPhoto && bitmap != null) {
                val pathImageSaved = saveImagePetUseCase.invoke(
                    bitmap,
                    pet.imageUrl,
                )
                pet.copy(imageUrl = pathImageSaved ?: "")
            } else {
                pet
            }
            repository.updatePet(newPet)
            scheculeBirthPet(pet.birthAlarm)
        }
    }

    private fun scheculeBirthPet(alarmItem: NotificationAlarmItem) {
        alarmScheduler.scheduleRepeatAlarm(
            alarmItem = alarmItem,
            interval = TimeUnit.DAYS.toMillis(AlarmInterval.ONE_YEAR_IN_DAYS.value),
        )
    }
}
