package br.com.joaovq.mydailypet.pet.presentation.viewmodel

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.domain.usecases.CreatePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.UpdateInfosPetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateAnimalUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateDateUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateNameUseCase
import br.com.joaovq.mydailypet.pet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.pet.presentation.viewstate.AddPetUiState
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtil
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddPetViewModelTest {
    @get:Rule
    val testMainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: AddPetViewModel

    @RelaxedMockK
    private lateinit var createPetUseCase: CreatePetUseCase

    @MockK
    private lateinit var updateInfosPetUseCase: UpdateInfosPetUseCase

    @MockK
    private lateinit var validateName: ValidateNameUseCase

    @MockK
    private lateinit var validateAnimal: ValidateAnimalUseCase

    @MockK
    private lateinit var validateDate: ValidateDateUseCase

    private val submitSuccessAction = AddPetAction.Submit(
        name = TestUtil.pet.name,
        type = TestUtil.pet.breed,
        weight = TestUtil.pet.weight,
        sex = TestUtil.pet.sex,
        birth = TestUtil.pet.birth,
        animal = TestUtil.pet.animal ?: "",
        photoPath = TestUtil.pet.imageUrl,
        birthAlarm = TestUtil.pet.birthAlarm,
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AddPetViewModel(
            createPetUseCase,
            updateInfosPetUseCase,
            validateName,
            validateAnimal,
            validateDate,
            testMainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `GIVEN action submit WHEN dispatch intent THEN message Pets is added in db`() =
        runTest {
            coEvery { validateName.invoke(name = TestUtil.pet.name) } returns TestUtil.successfulValidateState
            coEvery { validateDate.invoke(date = TestUtil.pet.birth) } returns TestUtil.successfulValidateState
            coEvery { validateAnimal.invoke(animalText = TestUtil.pet.animal) } returns TestUtil.successfulValidateState
            coEvery { createPetUseCase(ofType(Pet::class)) } returns Unit
            viewModel.dispatchIntent(
                submitSuccessAction,
            )
            delay(3000)
            assertEquals(R.string.message_pet_was_added, viewModel.state.value.message)
        }

    @Test
    fun `GIVEN action submit WHEN dispatch intent throws exception THEN message Error in insert`() =
        runTest {
            coEvery { validateName.invoke(name = TestUtil.pet.name) } returns TestUtil.successfulValidateState
            coEvery { validateDate.invoke(date = TestUtil.pet.birth) } returns TestUtil.successfulValidateState
            coEvery { validateAnimal.invoke(animalText = TestUtil.pet.animal) } returns TestUtil.successfulValidateState
            coEvery { createPetUseCase.invoke(ofType(Pet::class)) } throws Exception("Error in insert")
            assertEquals(AddPetUiState(false), viewModel.state.value)
            viewModel.dispatchIntent(
                submitSuccessAction,
            )
            delay(3000)
            println(viewModel.validateStateAnimal.value)
            println(viewModel.validateStateName.value)
            println(viewModel.validateStateDate.value)
            println(viewModel.state.value)
            assertEquals("Error in insert", viewModel.state.value.exception?.message)

            coVerifyAll { createPetUseCase(ofType(Pet::class)) }
        }
}
