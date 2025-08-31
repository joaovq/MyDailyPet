package br.com.joaovq.repository

import br.com.joaovq.localdatasource.PetLocalDataSource
import br.com.joaovq.mappers.toDomain
import br.com.joaovq.mappers.toDto
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petLocalDataSource: PetLocalDataSource,
) : PetRepository {
    override fun getAll(): Flow<List<Pet>> =
        petLocalDataSource.getAll().map { pets -> pets.map { it.toDomain() } }

    override fun getById(id: Int): Flow<Pet> =
        petLocalDataSource.getById(id).map { it.toDomain() }

    override suspend fun updatePet(pet: Pet) {
        petLocalDataSource.update(pet.toDto())
    }

    override suspend fun deletePet(pet: Pet) {
        petLocalDataSource.delete(pet.toDto())
    }

    override suspend fun deleteAll() {
        petLocalDataSource.deleteAll()
    }

    override suspend fun insertPet(pet: Pet) {
        petLocalDataSource.insert(pet.toDto())
    }
}
