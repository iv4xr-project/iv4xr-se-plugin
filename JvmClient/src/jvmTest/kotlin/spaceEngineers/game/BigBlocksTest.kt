package spaceEngineers.game

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.blockDefinitionByType
import spaceEngineers.model.MountPoint
import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.mountPointToRealWorldPosition
import spaceEngineers.model.extensions.orientationTowardsMountPoint
import spaceEngineers.model.extensions.orientationUpTowardsMountPoint
import testhelp.JSON_RESOURCES_DIR
import testhelp.MockOrRealGameTest
import testhelp.assertMountPointEquals
import testhelp.spaceEngineers
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


fun MountPoint.similar(mp: MountPoint, delta: Float = 0.1f): Boolean {
    return start.similar(mp.start, delta) && end.similar(mp.end, delta) && normal.similar(mp.normal, delta)
}


fun SpaceEngineers.doMountPoints(
    blockType: String = "LargeBlockRadioAntennaDish",
    otherBlock: String = "LargeHeavyBlockArmorBlock",
    yOffset: Float = 15f
) {
    val orientationForward = Vec3(0, 1, -1).normalized()
    val orientationUp = Vec3.RIGHT
    //val orientationForward = Vec3.LEFT
    //val orientationUp = Vec3.FORWARD


    val position = Vec3(0.0, 0.0 + yOffset, 0.0)
    character.teleport(position.copy(z = 15f))
    items.placeAt(
        blockType,
        position = position,
        orientationUp = orientationUp,
        orientationForward = orientationForward,
    )
    val block = observer.observeBlocks().allBlocks.last { it.blockType == blockType }
    val definition = definitions.blockDefinitions().first { it.blockType == blockType }
    definition.mountPoints.forEach { mountpoint ->
        val realMountpointPosition = block.mountPointToRealWorldPosition(mountpoint, definition, offset = 1.25f * 1)
        val blockOrientationForward = block.orientationTowardsMountPoint(mountpoint)
        val blockOrientationUp = block.orientationUpTowardsMountPoint(mountpoint)

        character.teleport(
            block.mountPointToRealWorldPosition(mountpoint, definition, offset = 1.25f * 10) - blockOrientationUp * 1.5f,
            orientationUp = blockOrientationUp,
            orientationForward = blockOrientationForward
        )
        items.placeAt(
            otherBlock,
            position = realMountpointPosition.let { it.copy(y = it.y) },
            orientationUp = blockOrientationUp,
            orientationForward = blockOrientationForward
        )
        observer.observeBlocks().allBlocks.first { it.blockType == otherBlock }
        //character.teleport(realMountpointPosition, orientationUp, orientationForward)
    }
}

class BigBlocksTest : MockOrRealGameTest(DEFINITIONS_FILE, useMock = false) {


    @Test
    fun biggest() = testContext {
        definitions.blockDefinitions().filterForBig().sortedByDescending { it.size.size }
            .forEach {
                println("| ${it.blockType} | ")
            }

    }

    //this scenario tests best checking positions of mount points around blocks
    @Test
    fun testPlaceBlock1() = spaceEngineers {
        doMountPoints("LargeHeavyBlockArmorBlock", "LargeHeavyBlockArmorBlock", yOffset = -700f)
    }

    //I used this to check where are being blocks placed, then place block around then to visually confirm their position (offset issue)
    @Test
    fun characterPosition() = spaceEngineers {
        val blockType = "LargeRefinery"
        val definition = definitions.blockDefinitionByType(blockType)
        val offset = Vec3(900, 0, 0)
        val s = 2.5 + 2.5 / 2.0
        val sx = definition.size.x * s
        val sy = definition.size.y * s
        val sz = definition.size.z * s
        val p = listOf(
            Vec3(sx, sy, sz),
            Vec3(sx, sy, -sz),
            Vec3(sx, -sy, sz),
            Vec3(-sx, sy, sz),
            Vec3(sx, sy, 0.0),
            Vec3(sx, 0.0, sz),
            Vec3(0.0, sy, sz),
            Vec3(sx, 0.0, 0.0),
            Vec3(0.0, sy, 0.0),
            Vec3(0.0, 0.0, sz),
        )
        val positions = (p + p.map { -it }).map { it + offset }
        positions.forEach { position ->
            items.placeAt(blockType, position, Vec3.FORWARD, Vec3.UP)
        }
        character.teleport(Vec3.ZERO + offset)

    }

    @Test
    fun testLargeHeavyBlockArmorCornerInv() = testContext {
        val blockDefinition = definitions.blockDefinitions().first { it.blockType == "LargeHeavyBlockArmorCornerInv" }
        assertEquals(Vec3.ONE, blockDefinition.size)
        assertEquals(3, blockDefinition.buildProgressModels.size)
        assertEquals(9, blockDefinition.mountPoints.size)
        listOf(
            1, 2, 5

        ).map { cubeMountPoints[it] }.forEach { mountPoint ->
            assertTrue(
                blockDefinition.mountPoints.any { it.similar(mountPoint, delta = 0.2f) },
                "$mountPoint not found in ${blockDefinition.mountPoints}"
            )
        }
    }

    val cubeMountPoints = listOf(
        MountPoint(
            normal = Vec3(0, 0, -1),
            start = Vec3(1, 0, 0),
            end = Vec3(0, 1, 0)
        ),
        MountPoint(
            normal = Vec3(0, 0, 1),
            start = Vec3(0, 0, 1),
            end = Vec3(1, 1, 1)
        ),
        MountPoint(
            normal = Vec3(-1, 0, 0),
            start = Vec3(0, 0, 0),
            end = Vec3(0, 1, 1)
        ),
        MountPoint(
            normal = Vec3(1, 0, 0),
            start = Vec3(1, 0, 1),
            end = Vec3(1, 1, 0)
        ),
        MountPoint(
            normal = Vec3(0, -1, 0),
            start = Vec3(0, 0, 0),
            end = Vec3(1, 0, 1)
        ),
        MountPoint(
            normal = Vec3(0, 1, 0),
            start = Vec3(0, 1, 1),
            end = Vec3(1, 1, 0)
        ),
    )

    @Test
    fun testLargeHeavyBlockArmorBlock() = testContext {
        val blockDefinition = definitions.blockDefinitions().first { it.blockType == "LargeHeavyBlockArmorBlock" }
        assertEquals(Vec3.ONE, blockDefinition.size)
        assertEquals(3, blockDefinition.buildProgressModels.size)
        assertEquals(6, blockDefinition.mountPoints.size)
        cubeMountPoints.forEachIndexed { index, mountPoint ->
            assertMountPointEquals(
                mountPoint,
                blockDefinition.mountPoints[index]
            )

        }
    }

    @Test
    fun testLargestCubeBlocks() = testContext {
        val blockDefinition =
            definitions.blockDefinitions().filterForBig().filter { it.mountPoints.isNotEmpty() }
                .sortedByDescending { it.size.size }
        blockDefinition.forEach { println("${it.id} ${it.blockType} ${it.size} ${it.mountPoints.size}") }
    }

    @Test
    fun testCameraPositionOffset() = spaceEngineers {
        observer.observe().let { observation ->
            println(observation.position - observation.camera.position)
        }
    }

    @Test
    fun testLargeBlockRadioAntennaDish() = testContext {
        val blockDefinition = definitions.blockDefinitions().first { it.blockType == "LargeBlockRadioAntennaDish" }
        assertEquals(Vec3(5, 3, 5), blockDefinition.size)
        assertEquals(3, blockDefinition.buildProgressModels.size)
        assertEquals(1, blockDefinition.mountPoints.size)
        assertEquals(
            MountPoint(
                normal = Vec3(0, -1, 0),
                start = Vec3(2, 0, 2),
                end = Vec3(3, 0, 3)
            ),
            blockDefinition.mountPoints.first()
        )
    }

    companion object {
        const val DEFINITIONS_JSON = "block_definitions.json"
        val DEFINITIONS_FILE = File("$JSON_RESOURCES_DIR${DEFINITIONS_JSON}")
    }

}
