package spaceEngineers.model.extensions

import spaceEngineers.model.*

//also watch out for MyCubeDefinition.ModelOffset (not exposed in API) - 4 models have this value non-zero and it could be another obscure issue
val Block.centerPosition: Vec3
    get() = maxPosition / 2f + minPosition / 2f

val Block.rotationMatrix: RotationMatrix
    get() = RotationMatrix.fromPose(this)

fun Block.mountPointToRealWorldPosition(
    mountPoint: MountPoint,
    blockDefinition: BlockDefinition,
    offset: Float = 0f
): Vec3 {
    val positionRelativeToBlockCenter = (mountPoint.start + mountPoint.end) * 0.5f * blockDefinition.cubeSize.sideSize
    return pointFromCenter(
        blockDefinition,
        positionRelativeToBlockCenter + mountPoint.normal * offset
    )
}

fun Block.pointFromCenter(blockDefinition: BlockDefinition, positionRelativeToBlockCenter: Vec3): Vec3 {
    val centerOfBlock = (blockDefinition.size * 0.5f * blockDefinition.cubeSize.sideSize)
    val mountPointPositionWithinBlock = rotationMatrix * -(positionRelativeToBlockCenter - centerOfBlock)
    return (centerPosition - mountPointPositionWithinBlock)
}

fun Block.orientationTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3 {
    return (-rotationMatrix * (mountPoint.normal)).normalized()
}

fun Block.orientationUpTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3 {
    return (-rotationMatrix * ((mountPoint.start - mountPoint.end))).normalized()
}
