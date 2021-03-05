package spaceEngineers.commands

import spaceEngineers.model.Vec3


class MovementArgs @JvmOverloads
constructor(val movement: Vec3, val rotation3: Vec3 = Vec3.zero(), val roll: Float = 0f)