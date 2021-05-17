package spaceEngineers.game

import spaceEngineers.commands.*
import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3
import testhelp.controller
import java.lang.Thread.sleep
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
        sleep(50)
        interact(InteractionArgs(InteractionType.END_USE))
    }

    @Test
    fun setToolbarItems() = controller {
        interact(InteractionArgs(InteractionType.TOOLBAR_SET, 4, 0, "Welder2Item"))
        interact(InteractionArgs(InteractionType.TOOLBAR_SET, 5, 0, "AngleGrinder2Item"))
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
            println("Block id: " + block.id)
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

    @Test
    fun manyMovesAndRotationsTest() = controller {
        val moves = ArrayList<MovementArgs>()
        val addMoves = {
                movementArgs: MovementArgs, count: Int -> for (n in 0 until count) moves.add(movementArgs)
        }

        val stepBoost = 5 // Increase to 10 to 20 to slow down the movement (and see it better)
        val forwardArgs = MovementArgs(Vec3(0f, 0f, -1f))
        addMoves(forwardArgs, 3 * stepBoost)

        val rotateArgs = MovementArgs(Vec3.ZERO, Vec2(0f, 9f), 0f)
        addMoves(rotateArgs, 2 * stepBoost)
        addMoves(forwardArgs, 3 * stepBoost)
        addMoves(MovementArgs(Vec3(-1f, 0f, 0f)), 20 * stepBoost) // Left

        val rollArgs = MovementArgs(Vec3.ZERO, Vec2.ZERO, -2f)
        addMoves(rollArgs, 2 * stepBoost)

        val allArgs = MovementArgs(Vec3(0f, 0.7f, 0.2f), Vec2(5f, 7f), 1.5f)
        addMoves(allArgs, 4 * stepBoost)

        for (move in moves) {
            moveAndRotate(move)
        }
    }
}
