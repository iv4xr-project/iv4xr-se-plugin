package spaceEngineers.controller.extensions

fun Int.toNullIfNegative1(): Int? {
    return if (this == -1) {
        null
    } else {
        this
    }
}
