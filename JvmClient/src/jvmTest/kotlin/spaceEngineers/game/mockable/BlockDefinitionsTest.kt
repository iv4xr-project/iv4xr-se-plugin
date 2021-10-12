package spaceEngineers.game.mockable

import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.CubeSize
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.isSidePoint
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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


class BlockDefinitionsTest : MockOrRealGameTest(inMockResourcesDirectory("BlockDefinitionsTest.txt"), forceRealGame = true, loadScenario = false) {

    @Test
    fun printDefinitionsForBdd() = testContext {
        definitions.blockDefinitions().filterForScreenshots().map { it.definitionId.type }.toSet().forEach {
            println("| $it |")
        }
    }

    @Test
    fun allCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().size)
    }

    @Test
    fun allByIdCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().map { "${it.definitionId.id}${it.definitionId.type}" }.distinct().size)
    }

    @Test
    fun emptyTypeCount() = testContext {
        definitions.blockDefinitions().let { blockDefinitions ->
            assertEquals(
                13,
                blockDefinitions.filter { it.definitionId.type.isEmpty() }.map { "${it.definitionId.id}${it.definitionId.type}" }
                    .size
            )
            blockDefinitions.filter { it.definitionId.type.isEmpty() }.map { "${it.definitionId.id}-${it.definitionId.type}" }
                .forEach(::println)
        }
    }

    @Test
    fun enabledCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().count { it.enabled })
    }

    @Test
    fun publicCount() = testContext {
        assertEquals(589, definitions.blockDefinitions().count { it.public })
    }

    @Test
    fun availableInSurvivalCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().count { it.availableInSurvival })
    }

    @Test
    fun notPublic() = testContext {
        definitions.blockDefinitions().let {
            blockDefinitions ->
            blockDefinitions.filterNot { it.public }.map { it.definitionId.type }.forEach(::println)
            assertEquals(37, blockDefinitions.count { !it.public })

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
        definitions.blockDefinitions().let {
            blockDefinitions ->
            assertEquals(
                blockDefinitions.filterForBig()
                .filterSidePoints().size,
            272
            )
            blockDefinitions
                .filterForBig()
                .filterSidePoints()
                .filter { it.definitionId.id == "MyObjectBuilder_CubeBlock" }
                //.filter { it.mountPoints.any { it.default } }
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
                blockDefinitions.filterForBig()
                    .filterSidePoints().flatMap { it.mountPoints }.size,
                1582
            )
            assertEquals(
                blockDefinitions.filterForBig()
                    .filterSidePoints().flatMap { it.mountPoints.filter { it.enabled } }.size,
                1578
            )
        }
    }

    @Test
    fun blockTypesWithNoProgressModels() = testContext {
        assertEquals(
            definitions.blockDefinitions().filter { it.buildProgressModels.isEmpty() }.map { it.definitionId.type }.toSet(),
            setOf("Monolith", "Stereolith", "DeadAstronaut", "LargeDeadAstronaut")
        )
    }

    @Test
    fun large1x1x1() = testContext {
        assertEquals(
            definitions.blockDefinitions().filterForScreenshots().size,
            202
        )
    }

    @Test
    fun allSmallBlocks() = testContext {
        assertEquals(
            definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Small }.map { it.definitionId.type }.size,
            260
        )
    }

    @Test
    fun blockDefinitionsBySize() = testContext {
        val sizeToCount = definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Large }.map { it.size }
            .groupBy { it.length() }
            .map { it.value.first() to it.value.size }.toMap()
        assertEquals(
            sizeToCount, mapOf(
                Vec3F.ONE to 272,
                Vec3F(2, 1, 2) to 4,
                Vec3F(1, 6, 2) to 1,
                Vec3F(1, 2, 1) to 40,
                Vec3F(1, 3, 1) to 6,
                Vec3F(2, 2, 3) to 5,
                Vec3F(3, 3, 3) to 7,
                Vec3F(4, 2, 1) to 1,
                Vec3F(1, 4, 1) to 2,
                Vec3F(5, 3, 5) to 5,
                Vec3F(5, 2, 1) to 1,
                Vec3F(2, 4, 2) to 1,
                Vec3F(3, 2, 3) to 5,
                Vec3F(3, 1, 3) to 5,
                Vec3F(5, 2, 5) to 2,
                Vec3F(5, 5, 1) to 1,
                Vec3F(5, 3, 1) to 1,
                Vec3F(3, 2, 4) to 2,
                Vec3F(3, 3, 5) to 2,
                Vec3F(1, 2, 3) to 3
            )
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
