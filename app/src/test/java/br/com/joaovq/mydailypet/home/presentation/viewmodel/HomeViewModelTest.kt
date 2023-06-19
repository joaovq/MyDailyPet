package br.com.joaovq.mydailypet.home.presentation.viewmodel

import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSource
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verifyAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private lateinit var localDataSource: PetLocalDataSource

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        homeViewModel = HomeViewModel(localDataSource, UnconfinedTestDispatcher())
    }

    @Test
    fun `GIVEN request WHEN get pets THEN state success`() = runTest {
        coEvery { localDataSource.getAll() } returns flow {
            emit(listOf())
        }
        assertNull(homeViewModel.homeState.value)
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            homeViewModel.dispatchIntent(HomeAction.GetPets)
        }
        assertEquals(HomeUiState.Success(listOf()), homeViewModel.homeState.value)
        verifyAll { localDataSource.getAll() }
    }

    @Test
    fun `GIVEN action submit WHEN dispatch intent throws exception THEN state error`() = runTest {
        coEvery { localDataSource.getAll() } throws Exception()
        assertNull(homeViewModel.homeState.value)
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            homeViewModel.dispatchIntent(HomeAction.GetPets)
        }
        assertTrue(homeViewModel.homeState.value is HomeUiState.Error)
        verifyAll { localDataSource.getAll() }
    }
}
