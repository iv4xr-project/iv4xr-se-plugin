package spaceEngineers.mock

import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.model.Vec3
import testhelp.checkMockObservation
import testhelp.mockController
import kotlin.test.Test


class MoveAgentTest {

    @Test
    fun moveTest() = mockController {
        // Single move request is not actually visible on the character movement, see manyMovesTest below
        val observation = moveTowards(MoveTowardsArgs(Vec3(0f, 0f, -1f), false))
        checkMockObservation(observation)
    }

    @Test
    fun moveAndRotateTest() = mockController {
        // Single move request is not actually visible on the character movement, see manyMovesTest below
        val args = MovementArgs(Vec3(0f, 0f, -1f), Vec3(0.3f, 0f, 0f), 0f)
        val observation = moveAndRotate(args)
        checkMockObservation(observation)
    }

    @Test
    fun manyMovesTest() = mockController {
        val moves = ArrayList<Vec3>()
        val addMoves = { move: Vec3, count: Int -> for (n in 0 until count) moves.add(move) }
        val stepBoost = 1 // Increase to 10 to 20 to slow down the movement (and see it better)
        addMoves(Vec3(0f, 0f, -1f), 4 * stepBoost) // Forward
        addMoves(Vec3(1f, 0f, 0f), 1 * stepBoost) // Right
        addMoves(Vec3(0f, 0f, -1f), 2 * stepBoost)
        addMoves(Vec3(-1f, 0f, 0f), 2 * stepBoost) // Left
        moves.add(Vec3(0f, 0.5f, 0f)) // Up
        for (move in moves) {
            val observation = moveTowards(MoveTowardsArgs(move, false))
            checkMockObservation(observation)
        }
    }

    @Test
    fun manyMovesAndRotationsTest() = mockController {
        val moves = ArrayList<MovementArgs>()
        val addMoves =
            { movementArgs: MovementArgs, count: Int -> for (n in 0 until count) moves.add(movementArgs) }
        val stepBoost = 1 // Increase to 10 to 20 to slow down the movement (and see it better)
        val forwardArgs = MovementArgs(Vec3(0f, 0f, -1f))
        addMoves(forwardArgs, 3 * stepBoost)
        val rotateArgs = MovementArgs(Vec3.zero(), Vec3(0f, 9f, 0f), 0f)
        addMoves(rotateArgs, 2 * stepBoost)
        addMoves(forwardArgs, 3 * stepBoost)
        addMoves(MovementArgs(Vec3(-1f, 0f, 0f)), 20 * stepBoost) // Left
        val rollArgs = MovementArgs(Vec3.zero(), Vec3.zero(), -2f)
        addMoves(rollArgs, 2 * stepBoost)
        val allArgs = MovementArgs(Vec3(0f, 0.7f, 0.2f), Vec3(5f, 7f, 0f), 1.5f)
        addMoves(allArgs, 4 * stepBoost)
        for (move in moves) {
            val observation = moveAndRotate(move)
            checkMockObservation(observation)
        }
    }
}