package spaceEngineers.game

import spaceEngineers.controller.blockingRotateUntilOrientationForward
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3
import spaceEngineers.model.allBlocks
import testhelp.assertVecEquals
import testhelp.spaceEngineers
import java.lang.Thread.sleep
import kotlin.test.Test


class RotateTowardsPositionTest {


    @Test
    fun rotateTowardsPosition() = spaceEngineers {
        val scenarioId = "simple-place-grind-torch-with-tools"
        session.loadFromTestResources(scenarioId)
        sleep(1000)
        observer.observeNewBlocks()
        items.equip(ToolbarLocation(1, 0))
        sleep(1000)
        items.place()
        sleep(1000)
        items.equip(ToolbarLocation(9, 0))


        val type = "LargeHeavyBlockArmorBlock"
        val newBlocksObservation = observer.observeBlocks()
        val block = newBlocksObservation.allBlocks.first { it.blockType == type }
        val orientationTowardsBlock = (block.position - newBlocksObservation.position).normalized()
        assertVecEquals(Vec3(-0.997f, 0.066f, 0.02f), orientationTowardsBlock, diff = 0.1f)
        assertVecEquals(orientationTowardsBlock, newBlocksObservation.orientationForward, diff = 0.1f)

        character.blockingRotateUntilOrientationForward(
            finalOrientation = Vec3(0f, 0f, -1f),
            rotation = Vec2.ROTATE_RIGHT,
            maxTries = 999999
        )
        assertVecEquals(Vec3(0f, 0f, -1f), observer.observe().orientationForward)
    }
}
