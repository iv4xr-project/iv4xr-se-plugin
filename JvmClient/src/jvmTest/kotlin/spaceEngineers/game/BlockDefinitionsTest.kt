package spaceEngineers.game

import spaceEngineers.model.CubeSize
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.Vec3
import testhelp.JSON_RESOURCES_DIR
import testhelp.MockOrRealGameTest
import testhelp.preferMocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


val stringFilters = setOf("Symbol", "Neon", "Window")
fun BlockDefinition.isGoodForScreenshots(): Boolean {
    return enabled && cubeSize == CubeSize.Large &&
            size == Vec3.ONE &&
            buildProgressModels.isNotEmpty() &&
            stringFilters.none { blockType.contains(it) }
}

fun Iterable<BlockDefinition>.filterForScreenshots(): List<BlockDefinition> {
    return filter { it.isGoodForScreenshots() }
}


class BlockDefinitionsTest : MockOrRealGameTest(DEFINITIONS_FILE, useMock = preferMocking) {


    //@Test //uncomment to update definitions json file
    fun updateDefinitionsFile() = runAndWriteResponse {
        definitions.blockDefinitions()
    }

    @Test
    fun printDefinitionsForBdd() = testContext {
        definitions.blockDefinitions().filterForScreenshots().map { it.blockType }.toSet().forEach {
            println("| $it |")
        }
    }

    @Test
    fun allCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().size)
    }

    @Test
    fun allByIdCount() = testContext {
        assertEquals(626, definitions.blockDefinitions().map { "${it.id}${it.blockType}" }.distinct().size)
    }

    @Test
    fun emptyTypeCount() = testContext {
        assertEquals(
            13,
            definitions.blockDefinitions().filter { it.blockType.isEmpty() }.map { "${it.id}${it.blockType}" }
                .size
        )
        definitions.blockDefinitions().filter { it.blockType.isEmpty() }.map { "${it.id}-${it.blockType}" }
            .forEach(::println)
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
        definitions.blockDefinitions().filterNot { it.public }.map { it.blockType }.forEach(::println)
        assertEquals(37, definitions.blockDefinitions().count { !it.public })
    }


    @Test
    fun duplicates() = testContext {
        assertEquals(
            definitions.blockDefinitions().groupBy { it.blockType }.filter { it.value.size > 1 }.keys,
            setOf("DebugSphereLarge", "", "LargePistonBase", "SmallPistonBase")
        )
    }

    @Test
    fun blockTypesWithNoProgressModels() = testContext {
        assertEquals(
            definitions.blockDefinitions().filter { it.buildProgressModels.isEmpty() }.map { it.blockType }.toSet(),
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
            definitions.blockDefinitions().filter { it.cubeSize == CubeSize.Small }.map { it.blockType }.size,
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

    @Test
    fun sameIdForSmallLarge() = testContext {
        val blockDefinitions = definitions.blockDefinitions()
        assertEquals(
            blockDefinitions.first { it.blockType == "LargeHeavyBlockArmorBlock" }.id,
            blockDefinitions.first { it.blockType == "SmallHeavyBlockArmorBlock" }.id
        )

    }

    @Test
    fun groupedById() = testContext {
        definitions.blockDefinitions().filterForScreenshots().groupBy { it.id }
            .map { it.key to it.value.map { it.blockType } }.forEach(::println)
    }

    @Test
    fun differentIdForDifferentBlocks() = testContext {
        val blockDefinitions = definitions.blockDefinitions()
        assertNotEquals(
            blockDefinitions.first { it.blockType == "LargeHeavyBlockArmorBlock" }.id,
            blockDefinitions.first { it.blockType == "LargeBlockCockpitSeat" }.id
        )
        assertEquals(
            blockDefinitions.first { it.blockType == "LargeHeavyBlockArmorBlock" }.id,
            blockDefinitions.first { it.blockType == "LargeBlockArmorBlock" }.id
        )
    }

    companion object {
        const val DEFINITIONS_JSON = "block_definitions.json"
        val DEFINITIONS_FILE = File("$JSON_RESOURCES_DIR${DEFINITIONS_JSON}")
    }

}
