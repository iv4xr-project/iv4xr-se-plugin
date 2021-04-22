package spaceEngineers.game

import spaceEngineers.model.CubeSize
import spaceEngineers.model.SeBlockDefinition
import spaceEngineers.model.Vec3
import testhelp.spaceEngineersSimplePlaceGrindTorch
import kotlin.test.Test
import kotlin.test.assertEquals


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
    fun printDefinitionsForBdd() = spaceEngineersSimplePlaceGrindTorch {
        definitions.blockDefinitions().filterForScreenshots().map { it.blockType }.toSet().forEach {
            println("| $it |")
        }
    }

    @Test
    fun allDefinitions() = spaceEngineersSimplePlaceGrindTorch {
        assertEquals(626, definitions.blockDefinitions().size)
    }

    @Test
    fun duplicates() = spaceEngineersSimplePlaceGrindTorch {
        assertEquals(
            definitions.blockDefinitions().groupBy { it.blockType }.filter { it.value.size > 1 }.keys,
            setOf("DebugSphereLarge", "", "LargePistonBase", "SmallPistonBase")
        )
    }

    @Test
    fun blockTypesWithNoProgressModels() = spaceEngineersSimplePlaceGrindTorch {
        assertEquals(
            definitions.blockDefinitions().filter { it.buildProgressModels.isEmpty() }.map { it.blockType }.toSet(),
            setOf("Monolith", "Stereolith", "DeadAstronaut", "LargeDeadAstronaut")
        )
    }

    @Test
    fun large1x1x1() = spaceEngineersSimplePlaceGrindTorch {
        assertEquals(
            definitions.blockDefinitions().filterForScreenshots().size,
            202
        )
    }

    @Test
    fun allSmallBlocks() = spaceEngineersSimplePlaceGrindTorch {
        assertEquals(
            definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Small }.map { it.blockType }.size,
            260
        )
    }

    @Test
    fun blockDefinitionsBySize() = spaceEngineersSimplePlaceGrindTorch {
        val sizeToCount = definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Large }.map { it.size }
            .groupBy { it.length() }
            .map { it.value.first() to it.value.size }.toMap()
        assertEquals(
            sizeToCount, mapOf(
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
                Vec3(5, 3, 1) to 1,
                Vec3(3, 2, 4) to 2,
                Vec3(3, 3, 5) to 2,
                Vec3(1, 2, 3) to 3
            )
        )
    }

}