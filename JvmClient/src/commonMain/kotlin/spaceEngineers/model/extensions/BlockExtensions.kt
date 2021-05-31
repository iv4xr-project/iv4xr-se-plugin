package spaceEngineers.model.extensions

import spaceEngineers.model.*

fun Block.mountPointToRealWorldPosition(
    mountPoint: MountPoint,
    blockDefinition: BlockDefinition,
    offset: Float = 1.25f
): Vec3 {
    val block = this
    val matrix = RotationMatrix.fromPose(this)
    val positionWithinBlock = (mountPoint.start + mountPoint.end) * 0.5f * 2.5f
    val centerOfBlock = (blockDefinition.size * 0.5f * 2.5f);
    val mountPointPositionWithinBlock = matrix * -(positionWithinBlock - centerOfBlock + mountPoint.normal * offset)
    return (block.position - mountPointPositionWithinBlock)
}

fun Block.orientationTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3 {
    val matrix = RotationMatrix.fromForwardAndUp(orientationForward, orientationUp)
    return (-matrix * (mountPoint.normal)).normalized()
}

fun Block.orientationUpTowardsMountPoint(
    mountPoint: MountPoint,
): Vec3 {
    val matrix = RotationMatrix.fromForwardAndUp(orientationForward, orientationUp)
    return (-matrix * ((mountPoint.start - mountPoint.end))).normalized()
}
