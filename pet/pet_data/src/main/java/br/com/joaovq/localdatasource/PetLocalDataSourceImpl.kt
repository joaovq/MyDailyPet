package br.com.joaovq.localdatasource

import br.com.joaovq.core.data.localdatasource.LocalDataSource
import br.com.joaovq.data.local.dao.PetDao
import br.com.joaovq.model.PetDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

interface PetLocalDataSource : LocalDataSource<PetDto>

class PetLocalDataSourceImpl @Inject constructor(
    private val petDao: PetDao,
) : PetLocalDataSource {
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
