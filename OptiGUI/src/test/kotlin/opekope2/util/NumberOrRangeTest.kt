package opekope2.util

import kotlin.test.*
import opekope2.util.NumberOrRange.Companion.tryParse

class NumberOrRangeTest {
    @Test
    fun parseMalformedTest() {
        assertNull(tryParse("malformed"))
        assertNull(tryParse("-"))
        assertNull(tryParse("(1-5)"))
        assertNull(tryParse("(1)"))
        assertNull(tryParse("(-1)"))
        assertNull(tryParse("(1-)"))
        assertNull(tryParse("(-)"))
    }

    @Test
    fun parseNumberTest1() {
        val v = tryParse("1")

        assertNotNull(v)

        assertTrue(v.isNumber)
        assertFalse(v.isRange)

        assertNull(v.start)
        assertNull(v.end)
        assertEquals(1, v.value)
    }

    @Test
    fun parseNumberTest2() {
        val v = tryParse("-1")

        assertNotNull(v)

        assertTrue(v.isNumber)
        assertFalse(v.isRange)

        assertNull(v.start)
        assertNull(v.end)
        assertEquals(-1, v.value)
    }

    @Test
    fun parseRangeTest1() {
        val v = tryParse("1-5")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(1, v.start)
        assertEquals(5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest2() {
        val v = tryParse("(1)-5")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(1, v.start)
        assertEquals(5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest3() {
        val v = tryParse("1-(5)")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(1, v.start)
        assertEquals(5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest4() {
        val v = tryParse("(1)-(5)")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(1, v.start)
        assertEquals(5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest5() {
        val v = tryParse("(-1)-5")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(-1, v.start)
        assertEquals(5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest6() {
        val v = tryParse("1-(-5)")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(1, v.start)
        assertEquals(-5, v.end)
        assertNull(v.value)
    }

    @Test
    fun parseRangeTest7() {
        val v = tryParse("(-1)-(-5)")

        assertNotNull(v)

        assertFalse(v.isNumber)
        assertTrue(v.isRange)

        assertEquals(-1, v.start)
        assertEquals(-5, v.end)
        assertNull(v.value)
    }
}
