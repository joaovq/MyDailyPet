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
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
        name = TestUtilPet.pet.name,
        type = TestUtilPet.pet.breed,
        weight = TestUtilPet.pet.weight,
        sex = TestUtilPet.pet.sex,
        birth = TestUtilPet.pet.birth,
        animal = TestUtilPet.pet.animal ?: "",
        photoNameFile = TestUtilPet.pet.imageUrl,
        birthAlarm = TestUtilPet.pet.birthAlarm,
        bitmap = null,
    )

    private val updateSuccessAction = AddPetAction.EditPet(
        id = TestUtilPet.pet.id,
        name = TestUtilPet.pet.name,
        type = TestUtilPet.pet.breed,
        weight = TestUtilPet.pet.weight,
        sex = TestUtilPet.pet.sex,
        birth = TestUtilPet.pet.birth,
        animal = TestUtilPet.pet.animal ?: "",
        photoPath = TestUtilPet.pet.imageUrl,
        birthAlarm = TestUtilPet.pet.birthAlarm,
        bitmap = null,
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
            mockValidateUseCasesSuccess()
            coEvery { createPetUseCase(pet = ofType(Pet::class), bitmap = any()) } returns Unit
            viewModel.dispatchIntent(
                submitSuccessAction,
            )
            delay(3000)
            assertEquals(R.string.message_pet_was_added, viewModel.state.value.message)
        }

    @Test
    fun `GIVEN action submit WHEN dispatch intent throws exception THEN message Error in insert`() =
        runTest {
            mockValidateUseCasesSuccess()
            coEvery {
                createPetUseCase.invoke(
                    ofType(Pet::class),
                    null,
                )
            } throws Exception("Error in insert")
            assertEquals(AddPetUiState(false), viewModel.state.value)
            viewModel.dispatchIntent(
                submitSuccessAction,
            )
            delay(3000)
            assertEquals("Error in insert", viewModel.state.value.exception?.message)

            coVerifyAll { createPetUseCase(ofType(Pet::class), null) }
        }

    @Test
    fun `GIVEN action update WHEN updatePet() THEN state Success`() =
        runTest {
            mockValidateUseCasesSuccess()
            coEvery {
                updateInfosPetUseCase.invoke(
                    ofType(Pet::class),
                    null,
                )
            } returns Unit
            assertEquals(AddPetUiState(false), viewModel.state.value)
            viewModel.dispatchIntent(updateSuccessAction)
            delay(3000)
            assertEquals(
                AddPetUiState(
                    isSuccesful = true,
                    isLoading = false,
                    message = R.string.message_pet_updated,
                ),
                viewModel.state.value,
            )

            coVerifyAll {
                updateInfosPetUseCase.invoke(
                    ofType(Pet::class),
                    null,
                )
            }
        }

    @Test
    fun `GIVEN action update WHEN updatePet() THEN error with Exception`() =
        runTest {
            mockValidateUseCasesSuccess()
            coEvery {
                updateInfosPetUseCase.invoke(
                    ofType(Pet::class),
                    null,
                )
            } returns Unit
            assertEquals(AddPetUiState(false), viewModel.state.value)
            viewModel.dispatchIntent(updateSuccessAction)
            delay(3000)
            assertEquals(
                AddPetUiState(
                    isSuccesful = true,
                    isLoading = false,
                    message = R.string.message_pet_updated,
                ),
                viewModel.state.value,
            )

            coVerifyAll {
                updateInfosPetUseCase.invoke(
                    ofType(Pet::class),
                    null,
                )
            }
        }

    @Test
    fun `GIVEN action add pet WHEN createPet() THEN name is not valid`() =
        runTest {
            coEvery { validateName.invoke(name = TestUtilPet.pet.name) } returns TestUtilPet.errorValidateState
            coEvery { validateDate.invoke(date = TestUtilPet.pet.birth) } returns TestUtilPet.successfulValidateState
            coEvery { validateAnimal.invoke(animalText = TestUtilPet.pet.animal) } returns TestUtilPet.successfulValidateState
            assertEquals(AddPetUiState(false), viewModel.state.value)
            viewModel.dispatchIntent(updateSuccessAction)
            delay(3000)
            assertFalse(
                viewModel.validateStateName.value.isValid,
            )
            assertEquals(
                AddPetUiState(isLoading = false),
                viewModel.state.value,
            )
        }

    private fun mockValidateUseCasesSuccess() {
        coEvery { validateName.invoke(name = TestUtilPet.pet.name) } returns TestUtilPet.successfulValidateState
        coEvery { validateDate.invoke(date = TestUtilPet.pet.birth) } returns TestUtilPet.successfulValidateState
        coEvery { validateAnimal.invoke(animalText = TestUtilPet.pet.animal) } returns TestUtilPet.successfulValidateState
    }
}
