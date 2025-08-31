package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface GetAllPetsUseCase {
    operator fun invoke(): Flow<List<Pet>>
}

class GetAllPets @Inject constructor(
    private val petRepository: PetRepository,
) : GetAllPetsUseCase {
    override fun invoke(): Flow<List<Pet>> {
        return petRepository.getAll()
    }
}
