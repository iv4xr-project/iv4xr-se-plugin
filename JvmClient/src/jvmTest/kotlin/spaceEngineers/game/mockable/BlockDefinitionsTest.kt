package spaceEngineers.game.mockable

import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.CubeSize
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.isSidePoint
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

fun BlockDefinition.isGoodForBig(): Boolean {
    return enabled && cubeSize == CubeSize.Large &&
        buildProgressModels.isNotEmpty()
}

fun Iterable<BlockDefinition>.filterForBig(): List<BlockDefinition> {
    return filter { it.isGoodForBig() }
}

val stringFilters = setOf("Symbol", "Neon", "Window")
fun BlockDefinition.isGoodForScreenshots(): Boolean {
    return enabled && cubeSize == CubeSize.Large &&
        size == Vec3F.ONE &&
        buildProgressModels.isNotEmpty() &&
        stringFilters.none { definitionId.type.contains(it) }
}

fun Iterable<BlockDefinition>.filterForScreenshots(): List<BlockDefinition> {
    return filter { it.isGoodForScreenshots() }
}

fun Iterable<BlockDefinition>.filterSidePoints(): List<BlockDefinition> {
    return filter { it.mountPoints.any { mountPoint -> mountPoint.isSidePoint() } }
}

private const val EXPECTED_DEFINITION_COUNT = 897

class BlockDefinitionsTest : MockOrRealGameTest(inMockResourcesDirectory("BlockDefinitionsTest.txt")) {

    @Test
    fun printDefinitionsForBdd() = testContext {
        definitions.blockDefinitions().filterForScreenshots().map { it.definitionId.type }.toSet().forEach {
            println("| $it |")
        }
    }

    @Test
    fun allCount() = testContext {
        assertEquals(EXPECTED_DEFINITION_COUNT, definitions.blockDefinitions().size)
    }

    @Test
    fun eachBlockHasAtLeastOneComponent() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            blockDefinitions.forEach { blockDefinition ->
                assertTrue(blockDefinition.components.isNotEmpty())
                assertTrue(blockDefinition.components.all { component -> component.count > 0 })
            }
        }
    }

    @Test
    fun allByIdCount() = testContext {
        assertEquals(
            EXPECTED_DEFINITION_COUNT,
            definitions.blockDefinitions().map { "${it.definitionId.id}${it.definitionId.type}" }.distinct().size
        )
    }

    @Test
    fun emptyTypeCount() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            assertEquals(
                13,
                blockDefinitions.filter { it.definitionId.type.isEmpty() }
                    .map { "${it.definitionId.id}${it.definitionId.type}" }
                    .size
            )
            blockDefinitions.filter { it.definitionId.type.isEmpty() }
                .map { "${it.definitionId.id}-${it.definitionId.type}" }
                .forEach(::println)
        }
    }

    @Test
    fun enabledCount() = testContext {
        assertEquals(EXPECTED_DEFINITION_COUNT, definitions.blockDefinitions().count { it.enabled })
    }

    @Test
    fun publicCount() = testContext {
        assertEquals(851, definitions.blockDefinitions().count { it.public })
    }

    @Test
    fun availableInSurvivalCount() = testContext {
        assertEquals(EXPECTED_DEFINITION_COUNT, definitions.blockDefinitions().count { it.availableInSurvival })
    }

    @Test
    fun notPublic() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            blockDefinitions.filterNot { it.public }.map { it.definitionId.type }
            assertEquals(46, blockDefinitions.count { !it.public })
        }
    }

    @Test
    fun duplicates() = testContext {
        assertEquals(
            definitions.blockDefinitions().groupBy { it.definitionId.type }.filter { it.value.size > 1 }.keys,
            setOf("DebugSphereLarge", "", "LargePistonBase", "SmallPistonBase")
        )
    }

    @Test
    fun bigWithNiceMountPoints() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            assertEquals(
                395,
                blockDefinitions.filterForBig()
                    .filterSidePoints().size
            )
            blockDefinitions
                .filterForBig()
                .filterSidePoints()
                .filter { it.definitionId.id == "MyObjectBuilder_CubeBlock" }
                // .filter { it.mountPoints.any { it.default } }
                .forEach { blockDefinition ->
                    println("${blockDefinition.definitionId.id} ${blockDefinition.definitionId.type}")
                    println(blockDefinition.mountPoints)
                }
        }
    }

    @Test
    fun mountPointCount() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            assertEquals(
                2547,
                blockDefinitions.filterForBig()
                    .filterSidePoints().flatMap { it.mountPoints }.size
            )
            assertEquals(
                2541,
                blockDefinitions.filterForBig()
                    .filterSidePoints().flatMap { it.mountPoints.filter { it.enabled } }.size
            )
        }
    }

    @Test
    fun large1x1x1() = testContext {
        assertEquals(
            296,
            definitions.blockDefinitions().filterForScreenshots().size
        )
    }

    @Test
    fun allSmallBlocks() = testContext {
        assertEquals(
            384,
            definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Small }.map { it.definitionId.type }.size
        )
    }

    @Test
    fun sameIdForSmallLarge() = testContext {
        val blockDefinitions = definitions.blockDefinitions()
        assertEquals(
            blockDefinitions.first { it.definitionId.type == "LargeHeavyBlockArmorBlock" }.definitionId.id,
            blockDefinitions.first { it.definitionId.type == "SmallHeavyBlockArmorBlock" }.definitionId.id
        )
    }

    @Test
    fun groupedById() = testContext {
        definitions.blockDefinitions().filterForScreenshots().groupBy { it.definitionId.id }
            .map { it.key to it.value.map { it.definitionId.type } }.forEach(::println)
    }

    @Test
    fun differentIdForDifferentBlocks() = testContext {
        val blockDefinitions = definitions.blockDefinitions()
        assertNotEquals(
            blockDefinitions.first { it.definitionId.type == "LargeHeavyBlockArmorBlock" }.definitionId.id,
            blockDefinitions.first { it.definitionId.type == "LargeBlockCockpitSeat" }.definitionId.id
        )
        assertEquals(
            blockDefinitions.first { it.definitionId.type == "LargeHeavyBlockArmorBlock" }.definitionId.id,
            blockDefinitions.first { it.definitionId.type == "LargeBlockArmorBlock" }.definitionId.id
        )
    }
}
