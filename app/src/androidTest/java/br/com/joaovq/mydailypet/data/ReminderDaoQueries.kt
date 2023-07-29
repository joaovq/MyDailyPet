package br.com.joaovq.mydailypet.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.joaovq.data.local.dao.PetDao
import br.com.joaovq.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mappers.toDto
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import br.com.joaovq.mydailypet.testutil.TestUtilReminder
import br.com.joaovq.reminder_data.dao.ReminderDao
import br.com.joaovq.reminder_data.mappers.toDto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@SmallTest
@RunWith(AndroidJUnit4::class)
class ReminderDaoQueries {

    private lateinit var myDailyPetDatabase: MyDailyPetDatabase
    private lateinit var reminderDao: ReminderDao
    private lateinit var petDao: PetDao

    @Before
    fun setUp() {
        myDailyPetDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDailyPetDatabase::class.java,
        ).setQueryExecutor(Executors.newSingleThreadExecutor()).build()
        petDao = myDailyPetDatabase.petDao()
        reminderDao = myDailyPetDatabase.reminderDao()
    }

    @Test
    fun createReminder(): Unit = runBlocking {
        createInitialReminder()
        val remindersInDb = reminderDao.selectAll()
        assertEquals(2, remindersInDb.firstOrNull()?.size)
        reminderDao.deleteAllReminder()
    }

    @Test
    fun getById(): Unit = runBlocking {
        createInitialReminder()
        val remindersInDb = reminderDao.selectById(1)
        assertEquals("Put meet for nina", remindersInDb.firstOrNull()?.name)
        reminderDao.deleteAllReminder()
    }

    private suspend fun createInitialReminder() = runBlocking {
        petDao.insertPet(TestUtilPet.pet.toDto())
        reminderDao.insertReminder(TestUtilReminder.reminder.toDto())
        reminderDao.insertReminder(TestUtilReminder.reminder.copy(id = 2, name = "Shower").toDto())
    }

    @Test
    fun deleteByIdReminder(): Unit = runBlocking {
        createInitialReminder()
        reminderDao.deleteReminder(TestUtilReminder.reminder.toDto())
        val remindersInDb = reminderDao.selectAll().single()
        assertEquals(1, remindersInDb.size)
    }

    @Test
    fun deleteAll(): Unit = runBlocking {
        createInitialReminder()
        reminderDao.deleteAllReminder()
        val remindersInDb = reminderDao.selectAll().single()
        assertEquals(0, remindersInDb.size)
    }
}
