package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtilPet
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ValidateNameTest {
    lateinit var validateNameUseCase: ValidateNameUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        validateNameUseCase = ValidateName(UnconfinedTestDispatcher())
    }

    @Test
    fun `GIVEN string blank WHEN validate name THEN not is valid and message string cannot blank`() =
        runTest {
            val validateStateName = validateNameUseCase("")
            assertFalse(validateStateName.isValid)
            assertEquals(R.string.message_field_is_not_blank, validateStateName.errorMessage)
        }

    @Test
    fun `GIVEN string Nina WHEN validate name THEN is valid`() = runTest {
        val validateStateName = validateNameUseCase(TestUtilPet.pet.name)
        assertTrue(validateStateName.isValid)
    }

    @Test
    fun `GIVEN string with special characters WHEN validate name THEN is not valid`() = runTest {
        val validateStateName = validateNameUseCase("Nin@")
        assertFalse(validateStateName.isValid)
        assertEquals(
            R.string.message_field_havent_special_characters_or_numbers,
            validateStateName.errorMessage,
        )
    }

    @Test
    fun `GIVEN string upper limit character 30 WHEN validate name THEN is not valid`() = runTest {
        val stringUpperLimit =
            "aliquid leo consectetuer tincidunt vulputate elit expetendis tractatos elit sollicitudin eum luptatum platonem iudicabit bibendum sed ius phasellus porro quas eloquentiam vehicula nam oratio periculis intellegebat evertitur dapibus taciti mucius reprimique"
        val validateStateName = validateNameUseCase(stringUpperLimit)
        assertFalse(validateStateName.isValid)
        assertEquals(
            R.string.message_field_excedded_limit_characters,
            validateStateName.errorMessage,
        )
    }
}
