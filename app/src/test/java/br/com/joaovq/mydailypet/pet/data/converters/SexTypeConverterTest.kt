package br.com.joaovq.mydailypet.pet.data.converters

import br.com.joaovq.mydailypet.core.domain.model.SexType.FEMALE
import br.com.joaovq.mydailypet.core.domain.model.SexType.MALE
import br.com.joaovq.mydailypet.core.domain.model.SexType.NOT_IDENTIFIED
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
    fun `GIVEN NOT IDENTIFIED string WHEN to Sex Type THEN object NOT_IDENTIFIED`() {
        val sexType = sexTypeConverter.toSexType("NOT IDENTIFIED")
        assertEquals(NOT_IDENTIFIED, sexType)
    }

    @Test
    fun `GIVEN blank string string WHEN to Sex Type THEN object NOT_IDENTIFIED`() {
        val sexType = sexTypeConverter.toSexType("")
        assertEquals(NOT_IDENTIFIED, sexType)
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

    @Test
    fun `GIVEN object NOT_IDENTIFIED WHEN from Sex Type THEN string NOT IDENTIFIED`() {
        val sexType = sexTypeConverter.fromSexType(NOT_IDENTIFIED)
        assertEquals("NOT IDENTIFIED", sexType)
    }
}
