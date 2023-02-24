package spaceEngineers.model

interface NumberVec2<T : Number> : Vector<T> {
    val x: T
    val y: T

    override operator fun get(index: Int): T {
        return when (index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("$index, max $dimensions")
        }
    }

    override val dimensions: Int get() = 2
}

interface Vector<T : Number> {

    val dimensions: Int

    operator fun get(index: Int): T
}

interface NumberVec3<T : Number> : Vector<T> {
    val x: T
    val y: T
    val z: T

    override operator fun get(index: Int): T {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("$index, max $dimensions")
        }
    }

    override val dimensions: Int get() = 3
}
