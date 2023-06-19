package br.com.joaovq.mydailypet.addpet.presentation.viewmodel

import br.com.joaovq.mydailypet.addpet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.addpet.presentation.viewstate.AddPetUiState
import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSource
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddPetViewModelTest {
    @MockK(relaxed = true)
    private lateinit var localDataSource: PetLocalDataSource
    private lateinit var viewModel: AddPetViewModel

    @get:Rule
    val testMainDispatcherRule = MainDispatcherRule()

    private val petDto = PetDto()
    val action = AddPetAction.Submit(
        name = petDto.name,
        type = petDto.type,
        weight = petDto.weight,
        sex = petDto.sex,
        birth = petDto.birth,
        animal = petDto.animal ?: "",
        photoPath = petDto.imageUrl,
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AddPetViewModel(localDataSource, UnconfinedTestDispatcher())
    }

    @Test
    fun `GIVEN action submit WHEN dispatch intent THEN message Pets is added in db`() = runTest {
        coEvery { localDataSource.insert(petDto) } returns Unit

        assertEquals(AddPetUiState(false), viewModel.state.value)
        viewModel.dispatchIntent(
            action,
        )
        assertEquals("Pet is added in db", viewModel.state.value.message)

        coVerifyAll { localDataSource.insert(petDto) }
    }

    @Test
    fun `GIVEN action submit WHEN dispatch intent throws exception THEN message Error in insert`() = runTest {
        coEvery { localDataSource.insert(petDto) } throws Exception("Error in insert")

        assertEquals(AddPetUiState(false), viewModel.state.value)
        viewModel.dispatchIntent(
            action,
        )
        assertEquals("Error in insert", viewModel.state.value.message)

        coVerifyAll { localDataSource.insert(petDto) }
    }
}
