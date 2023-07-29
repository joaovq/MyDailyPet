package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.core.di.DefaultDispatcher
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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
            petRepository.getAll()
        }
    }
}
