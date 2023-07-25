package br.com.joaovq.mydailypet.home.presentation.viewmodel

import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.domain.usecases.DeletePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.GetAllReminderUseCase
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import br.com.joaovq.mydailypet.testutil.TestUtilReminder
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val testRule = MainDispatcherRule()

    @MockK(relaxed = true)
    private lateinit var getAllPetsUseCase: GetAllPetsUseCase

    @MockK
    private lateinit var getAllRemindersUseCase: GetAllReminderUseCase

    @MockK(relaxed = true)
    private lateinit var deletePetUseCase: DeletePetUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        homeViewModel = HomeViewModel(
            getAllPetsUseCase,
            getAllRemindersUseCase,
            deletePetUseCase,
            testRule.testDispatcher,
        )
    }

    @Test
    fun `GIVEN request WHEN get pets THEN state success`() = runTest {
        coEvery { getAllPetsUseCase() } returns flow {
            emit(listOf())
        }
        assertNull(homeViewModel.homeState.value)
        backgroundScope.launch(testRule.testDispatcher) {
            homeViewModel.dispatchIntent(HomeAction.GetPets)
        }
        assertEquals(HomeUiState.Success(listOf()), homeViewModel.homeState.value)
        coVerifyAll { getAllPetsUseCase() }
    }

    @Test
    fun `GIVEN action submit WHEN dispatch intent throws exception THEN state error`() = runTest {
        coEvery { getAllPetsUseCase() } throws Exception()
        assertNull(homeViewModel.homeState.value)
        backgroundScope.launch(testRule.testDispatcher) {
            homeViewModel.dispatchIntent(HomeAction.GetPets)
        }
        assertTrue(homeViewModel.homeState.value is HomeUiState.Error)
        coVerifyAll { getAllPetsUseCase() }
    }

    @Test
    fun `GIVEN action deletePet WHEN deletePet() THEN state Success`() = runTest {
        coEvery { getAllPetsUseCase() } returns emptyFlow()
        coEvery { deletePetUseCase(ofType(Pet::class)) } returns Unit
        backgroundScope.launch(testRule.testDispatcher) {
            homeViewModel.dispatchIntent(
                HomeAction.DeletePet(
                    TestUtilPet.pet,
                ),
            )
        }
        assertTrue(homeViewModel.homeState.value is HomeUiState.DeleteSuccess)
        coVerifyAll { deletePetUseCase(ofType(Pet::class)) }
    }

    @Test
    fun `GIVEN action deletePet WHEN deletePet() THEN throw Exception state Error`() = runTest {
        coEvery { deletePetUseCase(ofType(Pet::class)) } throws Exception()
        backgroundScope.launch(testRule.testDispatcher) {
            homeViewModel.dispatchIntent(
                HomeAction.DeletePet(
                    TestUtilPet.pet,
                ),
            )
        }
        assertTrue(homeViewModel.homeState.value is HomeUiState.Error)
        coVerifyAll { deletePetUseCase(ofType(Pet::class)) }
    }

    @Test
    fun `GIVEN action GetReminders WHEN getReminders() THEN list reminders`() = runTest {
        val reminders = listOf(TestUtilReminder.reminder)
        coEvery { getAllRemindersUseCase() } returns flow {
            emit(reminders)
        }
        assertEquals(listOf<Reminder>(), homeViewModel.reminders.value)
        backgroundScope.launch(testRule.testDispatcher) {
            homeViewModel.dispatchIntent(HomeAction.GetReminders)
        }
        assertEquals(reminders, homeViewModel.reminders.value)
        coVerifyAll { getAllRemindersUseCase() }
    }
}
