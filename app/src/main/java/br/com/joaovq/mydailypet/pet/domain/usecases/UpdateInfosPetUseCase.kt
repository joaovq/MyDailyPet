package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UpdateInfosPetUseCase {
    suspend operator fun invoke(pet: Pet)
}

class UpdateInfosPet @Inject constructor(
    private val repository: PetRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : UpdateInfosPetUseCase {
    override suspend fun invoke(pet: Pet) {
        withContext(dispatcher) {
            repository.updatePet(pet)
        }
    }
}
