package br.com.joaovq.pet_domain.repository

import br.com.joaovq.pet_domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getAll(): Flow<List<Pet>>
    suspend fun getById(id: Int): Flow<Pet>
    suspend fun updatePet(pet: Pet)
    suspend fun deletePet(pet: Pet)
    suspend fun deleteAll()
    suspend fun insertPet(pet: Pet)
}
