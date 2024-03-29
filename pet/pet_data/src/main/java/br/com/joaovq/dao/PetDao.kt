package br.com.joaovq.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.joaovq.model.PET_ID_COLUMN_INFO
import br.com.joaovq.model.PET_TABLE_NAME
import br.com.joaovq.model.PetDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petDto: PetDto)

    @Query("SELECT * FROM $PET_TABLE_NAME")
    fun getAll(): Flow<List<PetDto>>

    @Query("SELECT * FROM $PET_TABLE_NAME where $PET_ID_COLUMN_INFO =:id")
    fun getById(id: Int): Flow<PetDto>

    @Query(
        "SELECT * FROM $PET_TABLE_NAME WHERE name LIKE :name" +
            " OR nickname LIKE :name",
    )
    fun getByName(name: String): Flow<List<PetDto>>

    @Update
    suspend fun updatePet(petDto: PetDto)

    @Delete
    suspend fun deletePet(petDto: PetDto)

    @Query("DELETE FROM $PET_TABLE_NAME")
    suspend fun deleteAll()
}
