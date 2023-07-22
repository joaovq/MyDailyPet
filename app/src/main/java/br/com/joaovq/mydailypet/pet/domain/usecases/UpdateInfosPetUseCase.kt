package br.com.joaovq.mydailypet.pet.domain.usecases

import android.graphics.Bitmap
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

interface UpdateInfosPetUseCase {
    suspend operator fun invoke(pet: Pet,bitmap: Bitmap?)
}

class UpdateInfosPet @Inject constructor(
    private val repository: PetRepository,
    private val saveImagePetUseCase: SaveImagePetUseCase,
    private val alarmScheduler: AlarmScheduler,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : UpdateInfosPetUseCase {
    override suspend fun invoke(pet: Pet,bitmap: Bitmap?) {
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
