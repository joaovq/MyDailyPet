package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.mappers.toDomain
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetPetUseCase {
    suspend operator fun invoke(id: Int): Flow<Pet>
}

class GetPet @Inject constructor(
    private val petRepository: PetRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetPetUseCase {
    override suspend fun invoke(id: Int): Flow<Pet> {
        return withContext(dispatcher) {
            petRepository.getById(id).map { it.toDomain() }
        }
    }
}
