package spaceEngineers.commands

import spaceEngineers.model.Vec3


//TODO: name better on both sides (or maybe obsolete completely?)
class MoveTowardsArgs(
    val object1: Vec3,
    val object2: Boolean
)