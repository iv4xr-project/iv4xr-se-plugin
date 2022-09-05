package testrail

import kotlin.test.Test
import kotlin.test.assertEquals

class TestRailCaseTagsParserTest {

    @Test
    fun singleTag() {
        assertEquals(listOf("tag"), parseTags("[tag]"))
    }

    @Test
    fun twoTags() {
        assertEquals(listOf("tag", "tag2"), parseTags("[tag][tag2]"))
    }

    @Test
    fun spacing() {
        assertEquals(listOf("tag", "tag2"), parseTags("[tag] [tag2]"))
    }

    @Test
    fun withTitle() {
        assertEquals(listOf("tag", "tag2"), parseTags("[tag] [tag2]SomeTitle"))
    }

    @Test
    fun duplicates() {
        assertEquals(listOf("tag", "tag2", "tag"), parseTags("[tag] [tag2][tag]"))
    }

    @Test
    fun realExample1() {
        assertEquals(listOf("todo"), parseTags("[todo]Unequipping tools returns to the basic character animation"))

    }

    @Test
    fun upperCase() {
        assertEquals(listOf("Creative"), parseTags("[Creative]"))
    }

    @Test
    fun special() {
        assertEquals(listOf("$^˘°"), parseTags("[$^˘°]"))
    }

    fun creative() {

        assertEquals(listOf("Creative"), parseTags("[Creative] Can be used for action freely when in Creative"))
    }

}
