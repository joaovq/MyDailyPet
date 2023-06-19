package br.com.joaovq.mydailypet.core.util.extension

import junit.framework.TestCase.assertEquals
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
}
