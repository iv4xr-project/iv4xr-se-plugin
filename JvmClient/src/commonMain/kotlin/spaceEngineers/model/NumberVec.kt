package spaceEngineers.model

interface NumberVec2<out T : Number> {
    val x: T
    val y: T
}

interface NumberVec<out T : Number> {
    val x: T
    val y: T
    val z: T
}
