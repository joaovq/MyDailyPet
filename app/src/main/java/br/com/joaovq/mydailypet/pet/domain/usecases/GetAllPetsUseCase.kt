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

interface GetAllPetsUseCase {
    suspend operator fun invoke(): Flow<List<Pet>>
}

class GetAllPets @Inject constructor(
    private val petRepository: PetRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : GetAllPetsUseCase {
    override suspend fun invoke(): Flow<List<Pet>> {
        return withContext(dispatcher) {
            petRepository.getAll().map { pets -> pets.map { petDto -> petDto.toDomain() } }
        }
    }
}
