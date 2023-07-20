package br.com.joaovq.mydailypet.pet.data.localdatasource

import br.com.joaovq.mydailypet.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

interface PetLocalDataSource : LocalDataSource<PetDto>

class PetLocalDataSourceImpl @Inject constructor(
    private val petDao: PetDao,
): PetLocalDataSource {
    override fun getAll(): Flow<List<PetDto>> {
        return try {
            petDao.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    override fun getById(id: Int): Flow<PetDto> {
        return try {
            petDao.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    override suspend fun update(entity: PetDto) {
        try {
            petDao.updatePet(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteAll() {
        try {
            petDao.deleteAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(entity: PetDto) {
        try {
            petDao.deletePet(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun insert(entity: PetDto) {
        try {
            petDao.insertPet(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
