package spaceEngineers.model.extensions

import spaceEngineers.model.*

//also watch out for MyCubeDefinition.ModelOffset (not exposed in API) - 4 models have this value non-zero and it could be another obscure issue
val Block.centerPosition: Vec3
    get() = maxPosition / 2f + minPosition / 2f

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
    return (block.centerPosition - mountPointPositionWithinBlock)
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
