package testhelp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import spaceEngineers.model.SeObservation
import spaceEngineers.model.Vec3

fun checkMockObservation(obs: SeObservation?) {
    assertNotNull(obs)
    obs?.let { observation ->
        assertEquals("Mock", observation.agentID)
        assertEquals(Vec3(4.0f, 2.0f, 0.0f), observation.position)
        observation.entities.first().let { entity ->
            assertEquals("Ente", entity.id)
            assertEquals(Vec3(3f, 2f, 1f), entity.position)
        }
        observation.grids.first().let { grid ->
            assertEquals(1, grid.blocks.size)
            grid.blocks.first().let { block ->
                assertEquals("blk", block.id)
                assertEquals(10f, block.maxIntegrity)
                assertEquals(1f, block.buildIntegrity)
                assertEquals(5f, block.integrity)
                assertEquals("MockBlock", block.blockType)
                assertEquals(Vec3(5f, 5f, 5f), block.position)
            }
        }
    }
}