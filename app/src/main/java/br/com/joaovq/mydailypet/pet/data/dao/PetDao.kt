package br.com.joaovq.mydailypet.pet.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petDto: PetDto)

    @Query("SELECT * FROM pet_tb")
    fun getAll(): Flow<List<PetDto>>

    @Query(
        "SELECT * FROM pet_tb WHERE name LIKE :name" +
            " OR nickname LIKE :name",
    )
    fun getByName(name: String): Flow<List<PetDto>>

    @Update
    suspend fun updatePet(petDto: PetDto)

    @Delete
    suspend fun deletePet(petDto: PetDto)

    @Query("DELETE FROM pet_tb")
    suspend fun deleteAll()
}
