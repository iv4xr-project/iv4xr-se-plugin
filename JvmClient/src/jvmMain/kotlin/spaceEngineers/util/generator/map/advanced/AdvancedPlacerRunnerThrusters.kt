package spaceEngineers.util.generator.map.advanced

import spaceEngineers.controller.ExtendedSpaceEngineers
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.typedBlockByCustomName
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.util.generator.map.Orientations

fun main() {
    val batch = SpaceEngineersJavaProxyBuilder().localhost()
    val se = batch.extend()
    se.removeAllBlocks()
    val width = 5
    val height = 10
    val f = MutableCell.WALL.copy(color = Color.BLUE)
    val ceiling = MutableCell.WALL.copy(color = null)
    val w = MutableCell.WALL.copy()

    val map = mapBuilder {
        cube(start = Vec3I.ZERO, end = Vec3I(width - 1, 2, height - 1), cell = w)
        removeCube(start = Vec3I.ONE.copy(y = 0), end = Vec3I(width - 2, 5, height - 2))
        floor(0).fill(f)
        floor(1).fill(f)
        with(floor(2)) {
            // remove(x = 2..5, z = 0)
            add(1, 1..height - 2, w)
            add(3, 1..height - 2, w)
            remove(2, 0)
            remove(1, 1)
            remove(1, 3)
            remove(1, 5)
            add(
                0, 1,
                w.copy(
                    // DefinitionIds.Thrust.LargeBlockSmallHydrogenThrust,
                    id = DefinitionIds.Thrust.LargeBlockSmallThrust,
                    orientations = listOf(Orientations(up = Vec3I.FORWARD, forward = Vec3I.RIGHT)),
                    customName = "Thr1",
                )
            )
            add(
                0, 2,
                w.copy(
                    id = DefinitionIds.TimerBlock.TimerBlockLarge,
                    customName = "Time1",
                )
            )
            add(
                0, 3,
                w.copy(
                    // DefinitionIds.Thrust.LargeBlockSmallHydrogenThrust,
                    id = DefinitionIds.Thrust.LargeBlockSmallThrust,
                    orientations = listOf(Orientations(up = Vec3I.FORWARD, forward = Vec3I.RIGHT)),
                    customName = "Thr2",
                )
            )
            add(
                0, 4,
                w.copy(
                    id = DefinitionIds.TimerBlock.TimerBlockLarge,
                    customName = "Time2",
                )
            )
            add(
                0, 5,
                w.copy(
                    // DefinitionIds.Thrust.LargeBlockSmallHydrogenThrust,
                    id = DefinitionIds.Thrust.LargeBlockSmallThrust,
                    orientations = listOf(Orientations(up = Vec3I.FORWARD, forward = Vec3I.RIGHT)),
                    customName = "Thr3",
                )
            )
            add(
                0, 6,
                w.copy(
                    id = DefinitionIds.TimerBlock.TimerBlockLarge,
                    customName = "Time3",
                )
            )
        }
        add(Vec3I.ZERO, w.copy(DefinitionIds.Reactor.LargeBlockSmallGenerator, priority = 10))
        add(Vec3I(0, 1, 0), w.copy(DefinitionIds.GravityGenerator.EMPTY, priority = 12))
    }

    se.admin.character.teleport(
        position = Vec3F(LARGE_BLOCK_CUBE_SIDE_SIZE * 2, LARGE_BLOCK_CUBE_SIDE_SIZE * 2, -10f),
        orientationUp = Vec3F.UP,
        orientationForward = Vec3F.BACKWARD
    )
    val advancedPlacer = AdvancedPlacer(se = se, map = map, start = Vec3I.ZERO)
    advancedPlacer.place()
    se.observer.observeBlocks().allBlocks.filterIsInstance<Thrust>().forEach {
        se.admin.blocks.thrust.setThrustOverride(it.id, 3456000f)
    }

    se.mapRepeatingTimerToBlock("Time1", "Thr1", 1.5f)
    se.mapRepeatingTimerToBlock("Time2", "Thr2", 3f)
    se.mapRepeatingTimerToBlock("Time3", "Thr3", 6f)
}

private fun ExtendedSpaceEngineers.mapRepeatingTimerToBlock(
    time1: String,
    thr1: String,
    triggerDelay: Float,
    action: String = "OnOff",
) {
    mapRepeatingTimerToBlock(
        observer.observeBlocks().typedBlockByCustomName<TimerBlock>(time1),
        observer.observeBlocks().typedBlockByCustomName<Thrust>(thr1),
        triggerDelay,
        action = action,
    )
}

private fun ExtendedSpaceEngineers.mapRepeatingTimerToBlock(
    time1: TimerBlock,
    thr1: Thrust,
    triggerDelay: Float,
    action: String,
) {
    admin.blocks.timerBlock.setTriggerDelay(time1.id, triggerDelay)
    admin.blocks.mapButtonToBlock(
        time1.id, 0, action, thr1.id,
    )
    admin.blocks.mapButtonToBlock(
        time1.id, 1, "Start", time1.id,
    )
    admin.blocks.timerBlock.start(time1.id)
}
