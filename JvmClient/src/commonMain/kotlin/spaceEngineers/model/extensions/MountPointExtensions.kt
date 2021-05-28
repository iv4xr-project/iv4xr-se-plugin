package spaceEngineers.model.extensions

import spaceEngineers.model.MountPoint
import kotlin.math.abs

fun MountPoint.isSidePoint(): Boolean {
    return abs(normal.x).toInt() == 1 || abs(normal.z).toInt() == 1
}

fun MountPoint.similar(mp: MountPoint, delta: Float = 0.1f): Boolean {
    return start.similar(mp.start, delta) && end.similar(mp.end, delta) && normal.similar(mp.normal, delta)
}
