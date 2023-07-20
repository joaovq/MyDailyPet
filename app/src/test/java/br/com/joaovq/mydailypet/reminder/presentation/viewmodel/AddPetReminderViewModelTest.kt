package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.CreateReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateFieldText
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.AddReminderEvents
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.AddReminderUiState
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtil
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
    fun `GIVEN intent Submit WHEN dispatch intent THEN state SubmittedSuccess`() = runTest {
        coEvery { validateFieldText.invoke(TestUtil.reminder.name) } returns TestUtil.successfulValidateState
        coEvery { validateFieldText.invoke(TestUtil.reminder.description) } returns TestUtil.successfulValidateState
        coEvery { validateTimerUseCase.invoke(TestUtil.reminder.toDate) } returns TestUtil.successfulValidateState

        reminderViewModel.dispatchIntent(
            AddReminderEvents.SubmitData(
                TestUtil.reminder.name,
                TestUtil.reminder.description,
                TestUtil.reminder.toDate,
                TestUtil.reminder.pet,
            ),
        )
        coJustAwait {
            createReminderUseCase.invoke(
                ofType(Reminder::class),
            )
        }
        assertEquals(AddReminderUiState.SubmittedSuccess, reminderViewModel.state.value)
    }
}
