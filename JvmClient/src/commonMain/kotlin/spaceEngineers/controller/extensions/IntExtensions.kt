package spaceEngineers.controller.extensions

fun Int.toNullIfMinusOne(): Int? {
    return takeIf { it > -1 }
}
