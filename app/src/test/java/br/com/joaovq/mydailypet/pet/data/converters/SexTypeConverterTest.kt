package br.com.joaovq.mydailypet.pet.data.converters


import br.com.joaovq.core.model.SexType.FEMALE
import br.com.joaovq.core.model.SexType.MALE
import br.com.joaovq.data.converters.SexTypeConverter
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import org.junit.Before
import org.junit.Test

class SexTypeConverterTest {
    private val sexTypeConverter: SexTypeConverter = SexTypeConverter()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `GIVEN FEMALE string WHEN to Sex Type THEN object FEMALE`() {
        val sexType = sexTypeConverter.toSexType("FEMALE")
        assertEquals(FEMALE, sexType)
    }

    @Test
    fun `GIVEN MALE string WHEN to Sex Type THEN object MALE`() {
        val sexType = sexTypeConverter.toSexType("MALE")
        assertEquals(MALE, sexType)
    }

    @Test
    fun `GIVEN MALE string WHEN to Sex Type THEN not equals`() {
        val sexType = sexTypeConverter.toSexType("FEMALE")
        assertNotSame(MALE, sexType)
    }

    @Test
    fun `GIVEN object MALE WHEN from Sex Type THEN string MALE`() {
        val sexType = sexTypeConverter.fromSexType(MALE)
        assertEquals("MALE", sexType)
    }

    @Test
    fun `GIVEN object FEMALE WHEN from Sex Type THEN string FEMALE`() {
        val sexType = sexTypeConverter.fromSexType(FEMALE)
        assertEquals("FEMALE", sexType)
    }
}
