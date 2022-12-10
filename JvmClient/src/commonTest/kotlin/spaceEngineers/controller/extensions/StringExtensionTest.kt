package spaceEngineers.controller.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringExtensionTest {
    fun testStripWhitespace() {
        assertEquals("aaaa", " a a a a ".stripWhitespace())
        assertEquals("abcd", " a  b   c     d ".stripWhitespace())
        assertEquals(
            "abcd",
            """
            a
            b
            c
            d
            """.trimIndent().stripWhitespace()
        )
    }

    @Test
    fun testWhitespaceEquals() {
        "aaaa".nonWhitespaceEquals(" a a a a ")
        "abcd".nonWhitespaceEquals(" a  b   c     d ")
        "abcd".nonWhitespaceEquals(
            """
            a
            b
            c
            d
            """.trimIndent()
        )
    }

    @Test
    fun `stripWhitespace removes whitespace characters from string`() {
        // Arrange
        val testString = "    a   b  c  d   e f g  h  i  j  k "

        // Act
        val result = testString.stripWhitespace()

        // Assert
        assertEquals("abcdefghijk", result)
    }

    @Test
    fun `stripWhitespace removes newline characters from string`() {
        // Arrange
        val testString = "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\n"

        // Act
        val result = testString.stripWhitespace()

        // Assert
        assertEquals("abcdefghijk", result)
    }

    @Test
    fun `stripWhitespace returns empty string when input string is empty`() {
        val testString = ""

        val result = testString.stripWhitespace()

        assertEquals("", result)
    }

    @Test
    fun `nonWhitespaceEquals returns true when strings are equal after whitespace is stripped`() {
        val testString1 = "   a   b  c  d   e f g  h  i  j  k   "
        val testString2 = "    a   b  c  d   e f g  h  i  j  k "

        val result = testString1.nonWhitespaceEquals(testString2)

        assertTrue(result)
    }

    @Test
    fun `nonWhitespaceEquals returns false when strings are not equal after whitespace is stripped`() {
        val testString1 = "   a   b  c  d   e f g  h  i  j  k   "
        val testString2 = "    a   b  c  d   e f g x h  i  j   k "

        val result = testString1.nonWhitespaceEquals(testString2)

        assertFalse(result)
    }

    @Test
    fun `stripWhitespace removes leading and trailing whitespace from string`() {
        val testString = "  abc  "

        val result = testString.stripWhitespace()

        assertEquals("abc", result)
    }

    @Test
    fun `nonWhitespaceEquals is case sensitive`() {
        val testString1 = "abc"
        val testString2 = "ABC"

        val result = testString1.nonWhitespaceEquals(testString2)

        assertFalse(result)
    }

    @Test
    fun `stripWhitespace removes tab characters from string`() {
        val testString = "\ta\tb\tc\t"

        val result = testString.stripWhitespace()

        assertEquals("abc", result)
    }

    @Test
    fun `stripWhitespace returns correct result when given string with only whitespace characters`() {
        val testString = "   \t\n\r"

        val result = testString.stripWhitespace()

        assertEquals("", result)
    }

    @Test
    fun `nonWhitespaceEquals returns false when one input string is empty`() {
        val testString1 = ""
        val testString2 = "abc"

        val result = testString1.nonWhitespaceEquals(testString2)

        assertFalse(result)
    }
}
