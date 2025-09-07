package interpreter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ValueTest {
    @Test
    fun testNumberValueToString() {
        val numberValue = NumberValue(42.0)
        assertEquals("42.0", numberValue.toStringValue())
    }

    @Test
    fun testNumberValueToStringWithDecimals() {
        val numberValue = NumberValue(3.14159)
        assertEquals("3.14159", numberValue.toStringValue())
    }

    @Test
    fun testNumberValueToStringWithZero() {
        val numberValue = NumberValue(0.0)
        assertEquals("0.0", numberValue.toStringValue())
    }

    @Test
    fun testNumberValueToStringWithNegative() {
        val numberValue = NumberValue(-10.5)
        assertEquals("-10.5", numberValue.toStringValue())
    }

    @Test
    fun testStringValueToString() {
        val stringValue = StringValue("Hello, World!")
        assertEquals("Hello, World!", stringValue.toStringValue())
    }

    @Test
    fun testStringValueToStringEmpty() {
        val stringValue = StringValue("")
        assertEquals("", stringValue.toStringValue())
    }

    @Test
    fun testStringValueToStringWithSpecialCharacters() {
        val stringValue = StringValue("Hello\nWorld\t!")
        assertEquals("Hello\nWorld\t!", stringValue.toStringValue())
    }

    @Test
    fun testNumberValueEquality() {
        val value1 = NumberValue(42.0)
        val value2 = NumberValue(42.0)
        val value3 = NumberValue(43.0)

        assertEquals(value1, value2)
        assertEquals(value1.hashCode(), value2.hashCode())
        assertEquals(false, value1 == value3)
    }

    @Test
    fun testStringValueEquality() {
        val value1 = StringValue("hello")
        val value2 = StringValue("hello")
        val value3 = StringValue("world")

        assertEquals(value1, value2)
        assertEquals(value1.hashCode(), value2.hashCode())
        assertEquals(false, value1 == value3)
    }

    @Test
    fun testNumberAndStringValueInequality() {
        val numberValue = NumberValue(42.0)
        val stringValue = StringValue("42.0")

        assertEquals(false, numberValue.equals(stringValue))
    }
}
