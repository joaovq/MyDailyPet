package br.com.joaovq.mydailypet.pet.domain.repository

import br.com.joaovq.mydailypet.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.pet.domain.mappers.toDto
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petLocalDataSource: LocalDataSource<PetDto>,
) : PetRepository {
    override suspend fun getAll(): Flow<List<PetDto>> =
        petLocalDataSource.getAll()

    override suspend fun getById(id: Int): Flow<PetDto> =
        petLocalDataSource.getById(id)

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
