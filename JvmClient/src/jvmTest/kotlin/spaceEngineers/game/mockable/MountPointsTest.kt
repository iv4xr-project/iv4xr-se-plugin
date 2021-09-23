package spaceEngineers.game.mockable

import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.CubeSize
import spaceEngineers.model.Vec3
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals


fun Iterable<BlockDefinition>.filterForMountPoints(): List<BlockDefinition> {
    return filter { it.cubeSize == CubeSize.Large && it.size == Vec3.ONE }
}

class MountPointsTest: MockOrRealGameTest(mockFile = inMockResourcesDirectory("MountPointsTest.txt")) {


    @Test
    fun empty() = testContext {
        definitions.blockDefinitions().filterForMountPoints().filter { it.mountPoints.isEmpty() }.map {
            it.blockType
        }.toSet().let { blockTypes ->
            assertEquals(
                blockTypes,
                setOf("OffroadRealWheel1x1", "OffroadRealWheel1x1mirrored", "RealWheel1x1", "RealWheel1x1mirrored")
            )
        }
    }

    @Test
    fun single() = testContext {
        definitions.blockDefinitions().filterForMountPoints().filter { it.mountPoints.size == 1 }.map {
            it.blockType
        }.toSet().let { blockTypes ->
            assertEquals(
                blockTypes.size,
                64
            )
        }
    }

    @Test
    fun bySize() = testContext {
        definitions.blockDefinitions().filterForMountPoints().groupBy { it.mountPoints.size }
            .map { it.key to it.value.size }.sortedBy { it.first }.let {
            println(it)
        }
    }
}