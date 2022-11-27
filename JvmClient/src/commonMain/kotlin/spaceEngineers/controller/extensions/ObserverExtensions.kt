package spaceEngineers.controller.extensions

import spaceEngineers.controller.Observer
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.largestGrid
import spaceEngineers.navigation.NavGraph

fun Observer.distanceTo(position: Vec3F): Float {
    val currentPosition = observe().position
    return (position - currentPosition).length()
}

fun Observer.navigationGraph(): NavGraph {
    return navigationGraph(observeBlocks().largestGrid().id)
}

