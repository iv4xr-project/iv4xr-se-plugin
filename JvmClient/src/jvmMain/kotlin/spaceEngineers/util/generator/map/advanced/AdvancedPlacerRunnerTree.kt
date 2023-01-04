package spaceEngineers.util.generator.map.advanced

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.model.Color
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.perpendicularVectors
import spaceEngineers.model.extensions.rotate
import spaceEngineers.model.extensions.roundToInt
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.util.generator.map.Orientations
import kotlin.math.roundToInt

fun main() {
    val batch = SpaceEngineersJavaProxyBuilder().fromStringLineReaderWriter(
        agentId = "",
        stringLineReaderWriter = SocketReaderWriter(host = "10.11.12.249"),
    )
    val se = batch.extend()
    se.removeAllBlocks()
    val f = MutableCell.WALL.copy(color = Color.BLUE)
    val ceiling = MutableCell.WALL.copy(color = null)
    val w = MutableCell.WALL.copy()
    val light = MutableCell.WALL.copy(
        id = DefinitionIds.InteriorLight.SmallLight, priority = 5,
        orientations = listOf(
            Orientations(forward = Vec3I.DOWN, up = Vec3I.RIGHT),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.LEFT),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.FORWARD),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.BACKWARD),
        )
    )

    val map = mapBuilder {
        makeTree(70, 35, w)

        // line(Vec3I.ZERO, Vec3I(10,7, 5), w, extraWidth = 0.05f)
        getBoundaries().let { b ->
            se.admin.character.teleport(
                position = Vec3F(
                    LARGE_BLOCK_CUBE_SIDE_SIZE * b.center.x, b.center.y * LARGE_BLOCK_CUBE_SIDE_SIZE * 1.5f, -120f
                ),
                orientationUp = Vec3F.UP, orientationForward = (Vec3F.BACKWARD + Vec3F.DOWN).normalized()
            )
        }
    }

    val advancedPlacer = AdvancedPlacer(se = se, map = map, start = Vec3I.ZERO)
    advancedPlacer.place()
}

fun MapBuilder.makeTree(height: Int, radius: Int, cell: MutableCell) {

    line(Vec3I.ZERO, Vec3I.UP * height, cell.copy(color = Color.fromByteRGB(83u, 53u, 10u)), extraWidth = 0.01f)
    (10..height step 5).forEach {
        val subradius = radius.toFloat() * ((height.toFloat() - it.toFloat() + 5) / (height.toFloat() - 10f)) + 5
        println("subradius: $subradius")
        generateBranches(Vec3I(0, it, 0), subradius.roundToInt(), cell)
    }
}

fun MapBuilder.generateBranches(start: Vec3I, radius: Int, cell: MutableCell) {

    Vec3F.FORWARD.perpendicularVectors(
        Vec3F.UP, random.nextInt(4, 7), offsetRad = random.nextInt(0, 100)
    ).forEach {
        // println(it.normalized())
        // println((it + Vec3F(0.0, -0.2, 0.0)).normalized())
        startBranch(
            position = start,
            direction = (it + Vec3F(0.0, -0.20 + random.nextDouble(-0.03, 0.03), 0.0)).normalized(),
            length = radius.toFloat(),
            cell = cell.copy(color = Color.PINE_TREE), level = 3,
        )
    }
}

fun MapBuilder.startBranch(
    direction: Vec3F,
    position: Vec3I,
    length: Float,
    cell: MutableCell,
    level: Int,
) {
    if (level <= 0) {
        return
    }
    val destination = position + (direction * length).roundToInt()
    // println("D: $destination")
    line(position, destination, cell)
    startBranch(
        direction = direction,
        position = destination,
        level = level - 1,
        length = length * (0.5f + random.nextDouble(-0.05, 0.05).toFloat()),
        cell = cell // .copy(color = Vec3F.RED)
    )
    startBranch(
        direction = direction.rotate(Vec3F.UP, Math.toRadians(random.nextDouble(40.0, 50.0)).toFloat()),
        position = destination,
        level = level - 1,
        length = length * (0.5f + random.nextDouble(-0.05, 0.05).toFloat()),
        cell = cell // .copy(color = Vec3F.GREEN)
    )
    startBranch(
        direction = direction.rotate(Vec3F.UP, Math.toRadians(random.nextDouble(-50.0, -40.0)).toFloat()),
        position = destination,
        level = level - 1,
        length = length * (0.5f + random.nextDouble(-0.05, 0.05).toFloat()),
        cell = cell // .copy(color = Vec3F.BLUE)
    )
}
