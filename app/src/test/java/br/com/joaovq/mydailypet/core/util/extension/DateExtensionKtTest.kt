package br.com.joaovq.mydailypet.core.util.extension

import android.os.Build
import androidx.annotation.RequiresApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateExtensionKtTest {
    @Test
    fun `GIVEN date from locale tag pt-br WHEN format date for string THEN String`() {
        val instance = Calendar.getInstance()
        instance.set(2000, 3, 10)
        val date = instance.time
        assertEquals(
            "10 de abr. de 2000",
            date.format(locale = Locale.forLanguageTag("pt-br")),
        )
    }

    @Test
    fun `GIVEN null any locale WHEN format date for string THEN String blank`() {
        val date: Date? = null
        assertEquals("", date.format())
    }

    @Test
    fun `GIVEN date from locale french WHEN format date for string THEN String`() {
        val instance = Calendar.getInstance()
        instance.set(2018, 0, 3)
        val date = instance.time
        assertEquals(
            "3 janv. 2018",
            date.format(
                locale = Locale.forLanguageTag("fr"),
            ),
        )
    }

    @Test
    fun `GIVEN date from locale spain WHEN format date for string THEN String`() {
        val instance = Calendar.getInstance()
        instance.set(2018, 0, 3)
        val date = instance.time
        assertEquals(
            "3 ene 2018",
            date.format(
                locale = Locale.forLanguageTag("es"),
            ),
        )
    }

    @Test
    fun `GIVEN today date WHEN Calendar compareSameDate(Date) THEN true`() {
        val calendarToDay = Calendar.getInstance()
        val sameDate = calendarToDay.compareSameDate(calendarToDay.time)
        assertTrue(sameDate)
    }

    @Test
    fun `GIVEN yesterday date compare with calendar today WHEN Calendar compareSameDate(Date) THEN false`() {
        val calendarToDay = Calendar.getInstance()
        val yesterdayCalendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, -1)
        }
        val sameDate = calendarToDay.compareSameDate(yesterdayCalendar.time)
        assertFalse(sameDate)
    }

    @Test
    @RequiresApi(value = Build.VERSION_CODES.O)
    fun `GIVEN yesterday date  WHEN Date calculateInterval() THEN equal days 1`() {
        val yesterdayCalendar = Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }
        yesterdayCalendar.time.calculateInterval { year, month, days ->
            assertEquals(1, days)
            assertEquals(0, month)
            assertEquals(0, year)
        }
    }

    @Test
    fun `GIVEN today date  WHEN Date calculateInterval() THEN equal days 0`() {
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time.calculateInterval { year, month, days ->
            assertEquals(0, days)
            assertEquals(0, month)
            assertEquals(0, year)
        }
    }

    @Test(expected = RuntimeException::class)
    @Throws(RuntimeException::class)
    fun `GIVEN one month plus current date  WHEN Date calculateInterval() THEN equal month equals 0`() {
        val yesterdayCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
        }
        yesterdayCalendar.time.calculateInterval { year, month, days ->
        }
    }
}
