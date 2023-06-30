package br.com.joaovq.mydailypet.data

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import br.com.joaovq.mydailypet.core.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import br.com.joaovq.mydailypet.pet.domain.mappers.toDto
import br.com.joaovq.mydailypet.core.domain.model.Pet
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class PetDaoQueriesTest {
    private var petDao: PetDao? = null
    private lateinit var db: MyDailyPetDatabase

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(applicationContext, MyDailyPetDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        petDao = db.petDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertPet() {
        runBlocking {
            val pet = Pet()
            var firstOrNull: List<PetDto>? = listOf()
            db.withTransaction {
                petDao?.insertPet(petDto = pet.toDto())
                petDao?.insertPet(petDto = pet.toDto())
                firstOrNull = petDao?.getAll()?.firstOrNull()
                println(firstOrNull)
            }
            assertEquals(2, firstOrNull?.size)
        }
    }

    @Test
    fun selectPets() {
        runBlocking {
            val firstOrNull = petDao?.getAll()?.firstOrNull()
            println(firstOrNull)
        }
    }

    @Test
    fun testDeletePet(): Unit = runBlocking {
        val pet = Pet(id = 2)
        quickInsert()
        db.withTransaction {
            petDao?.deletePet(petDto = pet.toDto())
        }
        val firstOrNull = petDao?.getAll()?.firstOrNull()
        println(firstOrNull)
        assertEquals(1, firstOrNull?.size)
    }

    @Test
    fun testDeleteAllPet(): Unit = runBlocking {
        val pet = Pet()
        quickInsert()
        petDao?.deleteAll()
        val firstOrNull = petDao?.getAll()?.firstOrNull()
        assertTrue(firstOrNull?.isEmpty() ?: false)
    }

    private fun quickInsert() = runBlocking {
        val pet = Pet()
        db.withTransaction {
            petDao?.insertPet(petDto = pet.toDto())
            petDao?.insertPet(petDto = pet.toDto())
        }
    }

    @After
    fun tearDown() {
        petDao = null
        db.close()
    }
}
