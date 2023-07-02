package br.com.joaovq.mydailypet.pet.data.localdatasource

import br.com.joaovq.mydailypet.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import io.mockk.verifyAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PetLocalDataSourceTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var localDataSource: LocalDataSource<PetDto>

    @MockK
    private lateinit var petDao: PetDao

    private lateinit var petDto: PetDto

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localDataSource = PetLocalDataSource(petDao)
        petDto = PetDto()
    }

    @Test
    fun `GIVEN nothing WHEN get all THEN empty flow`() {
        every { petDao.getAll() } returns emptyFlow()
        val flow = localDataSource.getAll()
        verify { petDao.getAll() }

        assertEquals(emptyFlow<List<PetDto>>(), flow)
        verifyAll { petDao.getAll() }
    }

    @Test
    @Throws(Exception::class)
    fun `GIVEN throws Exception WHEN get all THEN empty flow`() {
        every { petDao.getAll() } throws Exception()
        val flow = localDataSource.getAll()
        verify { petDao.getAll() }

        assertEquals(emptyFlow<List<PetDto>>(), flow)
        verifyAll { petDao.getAll() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN petDto WHEN update THEN Unit`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { petDao.updatePet(petDto = petDto) } returns Unit
        assertEquals(Unit, localDataSource.update(petDto))
        coVerifyAll { petDao.updatePet(petDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN petDto WHEN insert THEN Unit`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { petDao.insertPet(petDto = petDto) } returns Unit
        assertEquals(Unit, localDataSource.insert(petDto))
        coVerifyAll { petDao.insertPet(petDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @Throws(Exception::class)
    fun `GIVEN petDto WHEN insert THEN throws exception`() = runTest {
        coEvery { petDao.insertPet(petDto = petDto) } throws Exception()
        localDataSource.insert(petDto)
        coVerifyAll { petDao.insertPet(petDto) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN petDto WHEN delete THEN Unit`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { petDao.deletePet(petDto = petDto) } returns Unit
        assertEquals(Unit, localDataSource.delete(petDto))
        coVerifyAll { petDao.deletePet(petDto) }
    }
}