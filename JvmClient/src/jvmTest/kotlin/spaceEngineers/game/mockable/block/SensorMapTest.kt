package spaceEngineers.game.mockable.block

import spaceEngineers.model.DoorBase
import spaceEngineers.model.SensorBlock
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.blockById
import spaceEngineers.model.extensions.typedBlockById
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.movement.CompositeDirection3d
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class SensorMapTest : MockOrRealGameTest(scenarioId = "empty-world-creative") {

    @Test
    fun attachSensor() = testContext {
        admin.character.teleport(
            Vec3F.ZERO + Vec3F.BACKWARD * 2.5f + Vec3F.RIGHT * 2.5f + Vec3F.DOWN * 2.5f * 0.5f,
            orientationForward = Vec3F.FORWARD,
            orientationUp = Vec3F.UP,
        )
        val grid = admin.blocks.placeAt(
            DefinitionIds.AirtightSlideDoor.LargeBlockSlideDoor,
            position = Vec3F.ZERO,
        )

        val door = grid.blocks.first()
        val reactorId =
            admin.blocks.placeInGrid(
                DefinitionIds.Reactor.LargeBlockSmallGenerator,
                minPosition = Vec3I.RIGHT,
                gridId = grid.id
            )
        val reactor = observer.observeNewBlocks().blockById(reactorId)

        val armor = admin.blocks.placeInGrid(
            DefinitionIds.CubeBlock.LargeHeavyBlockArmorBlock,
            minPosition = Vec3I.RIGHT * 2,
            gridId = grid.id
        )

        val sensorId = admin.blocks.placeInGrid(
            DefinitionIds.SensorBlock.LargeBlockSensor,
            minPosition = -Vec3I.FORWARD + Vec3I.RIGHT * 2,
            gridId = grid.id,
            orientationForward = Vec3I.BACKWARD
        )
        admin.blocks.mapButtonToBlock(
            buttonBlockId = sensorId,
            buttonIndex = 0,
            targetId = door.id,
            action = "Open",
        )
        observer.observeNewBlocks().typedBlockById<SensorBlock>(sensorId).let { sensor ->
            assertEquals(Vec3F(-5, -5, -5), sensor.fieldMin)
            assertEquals(Vec3F(5, 5, 5), sensor.fieldMax)
        }
        assertTrue(observer.observeBlocks().typedBlockById<DoorBase>(door.id).open)
        extensions.character.vectorMovement.move(
            CompositeDirection3d.LEFT, ticks = 20
        )
        delay(2.seconds)
        assertFalse(observer.observeBlocks().typedBlockById<DoorBase>(door.id).open)
        extensions.character.vectorMovement.move(
            CompositeDirection3d.RIGHT, ticks = 20
        )
        delay(2.seconds)
        assertTrue(observer.observeBlocks().typedBlockById<DoorBase>(door.id).open)
        admin.blocks.sensorBlock.setFieldMin(sensorId, -Vec3F.ONE)
        admin.blocks.sensorBlock.setFieldMax(sensorId, Vec3F.ONE)
        observer.observeBlocks().typedBlockById<SensorBlock>(sensorId).let { sensor ->
            assertEquals(-Vec3F.ONE, sensor.fieldMin)
            assertEquals(Vec3F.ONE, sensor.fieldMax)
        }
    }
}
