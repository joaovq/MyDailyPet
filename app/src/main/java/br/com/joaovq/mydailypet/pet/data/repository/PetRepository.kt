package br.com.joaovq.mydailypet.pet.data.repository

import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getAll(): Flow<List<PetDto>>
    suspend fun getById(id: Int): Flow<PetDto>
    suspend fun updatePet(pet: Pet)
    suspend fun deletePet(pet: Pet)
    suspend fun deleteAll()
    suspend fun insertPet(pet: Pet)
}