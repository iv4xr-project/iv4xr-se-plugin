package spaceEngineers.controller.extensions

import spaceEngineers.controller.Observer
import spaceEngineers.model.Vec3F

fun Observer.distanceTo(position: Vec3F): Float {
    val currentPosition = observe().position
    return (position - currentPosition).length()
}
