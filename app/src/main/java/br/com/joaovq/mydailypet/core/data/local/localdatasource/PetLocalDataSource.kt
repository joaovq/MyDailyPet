package br.com.joaovq.mydailypet.core.data.local.localdatasource

import br.com.joaovq.mydailypet.core.data.local.dao.PetDao
import br.com.joaovq.mydailypet.core.data.local.dto.PetDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PetLocalDataSource @Inject constructor(
    private val petDao: PetDao,
) : LocalDataSource<PetDto> {
    override fun getAll(): Flow<List<PetDto>> {
        return try {
            petDao.getAll()
        } catch (e: Exception) {
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

    override suspend fun deleteAll(entity: PetDto) {
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
