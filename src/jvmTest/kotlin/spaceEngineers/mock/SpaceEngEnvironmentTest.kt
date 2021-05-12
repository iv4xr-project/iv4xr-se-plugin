package spaceEngineers.mock

import kotlin.test.*
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.controller.observe
import testhelp.checkMockObservation
import testhelp.mockController

class SpaceEngEnvironmentTest {
    @Test
    fun disconnectTest() = mockController {
    }

    @Test
    fun observeTest() = mockController {
        val observation = observe()
        checkMockObservation(observation)
        println("OrientationFwd: " + observation.orientationForward)
        println("OrientationUp : " + observation.orientationUp)
    }

    @Test
    fun observeManyTimesTest() = mockController {
        for (i in 0..4) {
            val observation = observe()
            checkMockObservation(observation)
        }
    }

    @Test
    fun observeBlocksTest() = mockController {
        val observation = observe(ObservationArgs(ObservationMode.BLOCKS))
        checkMockObservation(observation)
        val blocks = observation.grids.first().blocks
        assertNotNull(blocks)
        assertTrue(blocks.size > 0, "Expecting non-zero block count.")
        assertEquals(1, observation.grids.size, "Expecting 1 grid count.")
        assertEquals(1, observation.grids[0].blocks.size, "Expecting 1 grid block count.")
        println("Got " + blocks.size + " blocks.")
        val firstBlock = blocks.first()
        println("First block max integrity: " + firstBlock.maxIntegrity)
        println("First block build integrity: " + firstBlock.buildIntegrity)
        println("First block integrity: " + firstBlock.integrity)
    }
}
