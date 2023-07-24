package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.CreateReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateFieldText
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.AddReminderEvents
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderUiState
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import br.com.joaovq.mydailypet.testutil.TestUtilReminder
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustAwait
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddPetReminderViewModelTest {
    @get:Rule
    val testDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var reminderViewModel: AddPetReminderViewModel

    @MockK(relaxed = true)
    private lateinit var getAllPetsUseCase: GetAllPetsUseCase

    @RelaxedMockK
    private lateinit var createReminderUseCase: CreateReminderUseCase

    @RelaxedMockK
    private lateinit var validateTimerUseCase: ValidateDateTimeReminderUseCase

    @RelaxedMockK
    private lateinit var validateFieldText: ValidateFieldText

    private val submitActionSuccess = AddReminderEvents.SubmitData(
        TestUtilReminder.reminder.name,
        TestUtilReminder.reminder.description,
        TestUtilReminder.reminder.toDate,
        TestUtilReminder.reminder.pet,
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        reminderViewModel = AddPetReminderViewModel(
            getAllPetsUseCase,
            createReminderUseCase,
            validateTimerUseCase,
            validateFieldText,
            testDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `GIVEN intent Submit WHEN dispatch intent THEN exception`() = runTest {
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.name)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.description)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateTimerUseCase.invoke(TestUtilReminder.reminder.toDate)
        } returns TestUtilPet.successfulValidateState
        val exception = Exception()
        coEvery {
            createReminderUseCase.invoke(
                ofType(Reminder::class),
            )
        } throws exception
        reminderViewModel.dispatchIntent(submitActionSuccess)
        assertEquals(AddReminderUiState.Error(exception = exception), reminderViewModel.state.value)
    }

    @Test
    fun `GIVEN intent Submit WHEN dispatch intent THEN state SubmittedSuccess`() = runTest {
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.name)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.description)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateTimerUseCase.invoke(TestUtilReminder.reminder.toDate)
        } returns TestUtilPet.successfulValidateState
        reminderViewModel.dispatchIntent(submitActionSuccess)
        coJustAwait { createReminderUseCase.invoke(ofType(Reminder::class)) }
        assertEquals(AddReminderUiState.SubmittedSuccess, reminderViewModel.state.value)
    }

    @Test
    fun `GIVEN intent Submit WHEN createReminder() THEN state SubmittedSuccess`() = runTest {
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.name)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateFieldText.invoke(TestUtilReminder.reminder.description)
        } returns TestUtilPet.successfulValidateState
        coEvery {
            validateTimerUseCase.invoke(TestUtilReminder.reminder.toDate)
        } returns TestUtilPet.successfulValidateState
        reminderViewModel.dispatchIntent(submitActionSuccess)
        coJustAwait { createReminderUseCase.invoke(ofType(Reminder::class)) }
        assertEquals(AddReminderUiState.SubmittedSuccess, reminderViewModel.state.value)
    }
}
