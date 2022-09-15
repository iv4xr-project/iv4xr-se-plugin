package spaceEngineers.util

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilTest {

    @Test
    fun testStripWhitespace() {
        assertEquals("aaaa", " a a a a ".stripWhitespace())
        assertEquals("abcd", " a  b   c     d ".stripWhitespace())
        assertEquals("abcd", """
            a
            b
            c
            d
        """.trimIndent().stripWhitespace())
    }

    @Test
    fun testWhitespaceEquals() {
        "aaaa".nonWhitespaceEquals(" a a a a ")
        "abcd".nonWhitespaceEquals(" a  b   c     d ")
        "abcd".nonWhitespaceEquals("""
            a
            b
            c
            d
        """.trimIndent())
    }

}
