package spaceEngineers.navigation

import spaceEngineers.graph.BasicGraphSearch
import spaceEngineers.graph.GraphSearch
import spaceEngineers.graph.Path
import spaceEngineers.graph.toExtra
import spaceEngineers.model.ScreenName
import spaceEngineers.model.ScreenName.Companion.GamePlay
import spaceEngineers.model.ScreenName.Companion.JoinGame
import spaceEngineers.model.ScreenName.Companion.LoadGame
import spaceEngineers.model.ScreenName.Companion.Loading
import spaceEngineers.model.ScreenName.Companion.MainMenu
import spaceEngineers.model.ScreenName.Companion.ServerConnect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DirectedGraphSearchTest {

    private val search: GraphSearch<ScreenName, String> = BasicGraphSearch(DEFAULT_TRANSITIONS.toGraph().toExtra())

    private fun findPath(from: ScreenName, to: ScreenName): Path<String> {
        return search.findPath(from, to)
    }

    @Test
    fun testFailOnBadDestination1() {
        assertFails { findPath(Loading, MainMenu) }
    }

    @Test
    fun testToItself() {
        assertEquals(listOf(), findPath(MainMenu, MainMenu))
    }

    @Test
    fun testFailOnBadDestination2() {
        assertFails { findPath(MainMenu, Loading) }
    }

    @Test
    fun testShortestPaths() {
        assertEquals(listOf(MainMenu edgeTo LoadGame), findPath(MainMenu, LoadGame))
    }

    @Test
    fun testShortestPathReverse() {
        assertEquals(listOf(LoadGame edgeTo MainMenu), findPath(LoadGame, MainMenu))
    }

    @Test
    fun testLongerPath() {
        assertEquals(listOf(MainMenu edgeTo JoinGame, JoinGame edgeTo ServerConnect), findPath(MainMenu, ServerConnect))
    }

    @Test
    fun testNonExistent() {
        assertFails { findPath(GamePlay, ServerConnect) }
    }

}
