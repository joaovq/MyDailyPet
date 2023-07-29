package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetPetUseCase {
    suspend operator fun invoke(id: Int): Flow<Pet>
}

class GetPet @Inject constructor(
    private val petRepository: PetRepository,
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetPetUseCase {
    override suspend fun invoke(id: Int): Flow<Pet> {
        return withContext(dispatcher) {
            petRepository.getById(id)
        }
    }
}
