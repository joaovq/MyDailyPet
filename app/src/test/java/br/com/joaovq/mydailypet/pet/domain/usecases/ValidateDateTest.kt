package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class ValidateDateTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    var validateDate = ValidateDate(UnconfinedTestDispatcher())

    @Test
    fun `GIVEN yesterday date WHEN validate date THEN is valid`() = runTest {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, -1)
        }
        val validateState = validateDate.invoke(calendar.time)
        assertTrue(validateState.isValid)
    }

    @Test
    fun `GIVEN null WHEN validate date THEN is not valid`() = runTest {
        val validateState = validateDate.invoke(null)
        assertFalse(validateState.isValid)
    }

    @Test
    fun `GIVEN today date WHEN validate date THEN is valid`() = runTest {
        val calendar = Calendar.getInstance()
        val validateState = validateDate.invoke(calendar.time)
        assertTrue(validateState.isValid)
    }
}
