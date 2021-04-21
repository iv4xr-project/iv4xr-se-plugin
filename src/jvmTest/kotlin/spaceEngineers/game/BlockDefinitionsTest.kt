package spaceEngineers.game

import environments.closeIfCloseable
import spaceEngineers.controller.JsonRpcCharacterController
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.CubeSize
import spaceEngineers.model.SeBlockDefinition
import spaceEngineers.model.Vec3
import testhelp.TEST_AGENT
import kotlin.test.Test
import kotlin.test.assertEquals


var loaded = false

fun jsonRpcSpaceEngineers(
    scenarioId: String? = "simple-place-grind-torch",
    agentId: String = TEST_AGENT,
    spaceEngineers: JsonRpcCharacterController = JsonRpcCharacterController.localhost(agentId),
    block: JsonRpcCharacterController.() -> Unit
) {
    try {
        if (!loaded) {
            loaded = true
            scenarioId?.let {
                spaceEngineers.session.loadFromTestResources(scenarioId)
            }
        }
        block(spaceEngineers)
    } finally {
        spaceEngineers.closeIfCloseable()
    }
}


val stringFilters = setOf("Symbol", "Neon", "Window")
fun SeBlockDefinition.isGoodForScreenshots(): Boolean {
    return cubeSize == CubeSize.Large &&
            size == Vec3.ONE &&
            buildProgressModels.isNotEmpty() &&
            stringFilters.none { blockType.contains(it) }
}

fun Iterable<SeBlockDefinition>.filterForScreenshots(): List<SeBlockDefinition> {
    return filter { it.isGoodForScreenshots() }
}

class BlockDefinitionsTest {

    @Test
    fun printDefinitionsForBdd() = jsonRpcSpaceEngineers {
        blockDefinitions().filterForScreenshots().map { it.blockType }.toSet().forEach {
            println("| $it |")
        }
    }

    @Test
    fun allDefinitions() = jsonRpcSpaceEngineers {
        assertEquals(626, blockDefinitions().size)
    }

    @Test
    fun duplicates() = jsonRpcSpaceEngineers {
        assertEquals(
            blockDefinitions().groupBy { it.blockType }.filter { it.value.size > 1 }.keys,
            setOf("DebugSphereLarge", "", "LargePistonBase", "SmallPistonBase")
        )
    }

    @Test
    fun blockTypesWithNoProgressModels() = jsonRpcSpaceEngineers {
        assertEquals(
            blockDefinitions().filter { it.buildProgressModels.isEmpty() }.map { it.blockType }.toSet(),
            setOf("Monolith", "Stereolith", "DeadAstronaut", "LargeDeadAstronaut")
        )
    }

    @Test
    fun large1x1x1() = jsonRpcSpaceEngineers {
        assertEquals(
            blockDefinitions().filterForScreenshots().size,
            202
        )
    }

    @Test
    fun allSmallBlocks() = jsonRpcSpaceEngineers {
        assertEquals(
            blockDefinitions().filter { it.cubeSize == CubeSize.Small }.map { it.blockType }.size,
            260
        )
    }

    @Test
    fun blockDefinitionsBySize() = jsonRpcSpaceEngineers {
        val sizeToCount = blockDefinitions().filter { it.cubeSize == CubeSize.Large }.map { it.size }.groupBy { it.length() }
            .map { it.value.first() to it.value.size }.toMap()
        assertEquals(sizeToCount, mapOf(
            Vec3.ONE to 272,
            Vec3(2, 1, 2) to 4,
            Vec3(1, 6, 2) to 1,
            Vec3(1, 2, 1) to 40,
            Vec3(1, 3, 1) to 6,
            Vec3(2, 2, 3) to 5,
            Vec3(3, 3, 3) to 7,
            Vec3(4, 2, 1) to 1,
            Vec3(1, 4, 1) to 2,
            Vec3(5, 3, 5) to 5,
            Vec3(5, 2, 1) to 1,
            Vec3(2, 4, 2) to 1,
            Vec3(3, 2, 3) to 5,
            Vec3(3, 1, 3) to 5,
            Vec3(5, 2, 5) to 2,
            Vec3(5, 5, 1) to 1,
            Vec3(5,3,1) to 1,
            Vec3(3,2,4) to 2,
            Vec3(3, 3, 5) to 2,
            Vec3(1, 2, 3) to 3
        ))
    }

}