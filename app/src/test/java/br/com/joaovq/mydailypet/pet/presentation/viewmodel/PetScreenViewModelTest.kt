package br.com.joaovq.mydailypet.pet.presentation.viewmodel

import br.com.joaovq.mydailypet.pet.domain.usecases.GetPetUseCase
import br.com.joaovq.mydailypet.pet.presentation.viewintent.PetIntent
import br.com.joaovq.mydailypet.pet.presentation.viewstate.PetState
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PetScreenViewModelTest {
    @MockK
    private lateinit var getPetUseCase: GetPetUseCase
    private lateinit var petScreenViewModel: PetScreenViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        petScreenViewModel = PetScreenViewModel(
            getPetUseCase,
            UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `GIVEN id WHEN dispatch intent GetPet THEN success`() = runTest {
        coEvery { getPetUseCase(1) } returns flow {
            emit(TestUtilPet.pet)
        }
        assertEquals(null, petScreenViewModel.state.value)
        petScreenViewModel.dispatchIntent(PetIntent.GetPetDetails(1))
        assertEquals(PetState.Success(TestUtilPet.pet), petScreenViewModel.state.value)
        coVerifyAll { getPetUseCase(1) }
    }

    @Test
    fun `GIVEN 0 WHEN dispatch intent GetPet throws exception THEN error with message`() = runTest {
        coEvery { getPetUseCase(0) } throws Exception("Id not exists in db")
        assertEquals(null, petScreenViewModel.state.value)
        petScreenViewModel.dispatchIntent(PetIntent.GetPetDetails(0))
        assertEquals(PetState.Error("Id not exists in db"), petScreenViewModel.state.value)
        coVerifyAll { getPetUseCase(0) }
    }

    @Test
    fun `GIVEN 0 WHEN dispatch intent GetPet throws exception THEN error without message`() = runTest {
        coEvery { getPetUseCase(0) } throws Exception()
        assertEquals(null, petScreenViewModel.state.value)
        petScreenViewModel.dispatchIntent(PetIntent.GetPetDetails(0))
        assertEquals(PetState.Error("Error"), petScreenViewModel.state.value)
        coVerifyAll { getPetUseCase(0) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
