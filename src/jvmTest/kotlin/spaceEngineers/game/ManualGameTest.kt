package spaceEngineers.game

import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.InteractionType
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import testhelp.checkMockObservation
import testhelp.controller
import testhelp.mockController
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class ManualGameTest {

    @Test
    fun equipToolbarItemTest() = controller {
        val observation = interact(InteractionArgs(InteractionType.EQUIP, 4, -1, true))
        assertNotNull(observation)
    }

    @Test
    fun beginUseEndUseToolTest() = controller {
        interact(InteractionArgs(InteractionType.BEGIN_USE))
        Thread.sleep(50);
        interact(InteractionArgs(InteractionType.END_USE))
    }

    @Test
    fun checkNewBlock() = controller {
        var observation = observe(ObservationArgs(ObservationMode.NEW_BLOCKS))
        assertNotNull(observation)
        assertNotNull(observation.grids)
        println("Got " + observation.grids.size + " grids.")
        for (grid in observation.grids) {
            println("Got " + grid.blocks.size + " blocks.")
        }

        interact(InteractionArgs(InteractionType.EQUIP, 3, 0))
        interact(InteractionArgs(InteractionType.PLACE))
        observation = observe(ObservationArgs(ObservationMode.NEW_BLOCKS))
        assertTrue(observation.grids.isNotEmpty(), "Expecting non-zero grid count.")

        val blocks = observation.grids.map { it.blocks }.firstOrNull {
            it.isNotEmpty()
        } ?: error ("found no grid with non-empty blocks")

        for (block in blocks.subList(0, min(2, blocks.size))) {
            println("Block max integrity: " + block.maxIntegrity)
            println("Block build integrity: " + block.buildIntegrity)
            println("Block integrity: " + block.integrity)
            println("Block type: " + block.blockType)
            println("Block position min: " + block.minPosition)
            println("Block position max: " + block.maxPosition)
            println("Block size        : " + block.size)
            println("Block orientation fwd: " + block.orientationForward)
            println("Block orientation up : " + block.orientationUp)
        }
        assertEquals(1, blocks.size, "There should be exactly 1 new block.")
    }
}