package spaceEngineers.model.extensions

import spaceEngineers.model.Block
import spaceEngineers.model.BlockDefinition
import spaceEngineers.model.MountPoint
import spaceEngineers.model.RotationMatrix
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.Vec3F

// also watch out for MyCubeDefinition.ModelOffset (not exposed in API) - 4 models have this value non-zero and it could be another obscure issue
val Block.centerPosition: Vec3F
    get() = maxPosition / 2f + minPosition / 2f

val Block.rotationMatrix: RotationMatrix
    get() = RotationMatrix.fromPose(this)

fun Block.mountPointToRealWorldPosition(
    mountPoint: MountPoint,
    blockDefinition: BlockDefinition,
    offset: Float = 0f
): Vec3F {
    val positionRelativeToBlockCenter = (mountPoint.start + mountPoint.end) * 0.5f * blockDefinition.cubeSize.value
    return pointFromCenter(
        blockDefinition,
        positionRelativeToBlockCenter + mountPoint.normal * offset
    )
}

fun Block.pointFromCenter(blockDefinition: BlockDefinition, positionRelativeToBlockCenter: Vec3F): Vec3F {
    val centerOfBlock = (blockDefinition.size * 0.5f * blockDefinition.cubeSize.value)
    val mountPointPositionWithinBlock = rotationMatrix * -(positionRelativeToBlockCenter - centerOfBlock)
    return (centerPosition - mountPointPositionWithinBlock)
}

fun Block.orientationTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3F {
    return (-rotationMatrix * (mountPoint.normal)).normalized()
}

fun Block.orientationUpTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3F {
    return (-rotationMatrix * ((mountPoint.start - mountPoint.end))).normalized()
}

val Block.shortDescription: String
    get() = "$definitionId ($id) ${
    if (this is TerminalBlock) {
        customName
    } else {
        ""
    }
    }"
