package spaceEngineers.controller.proxy

interface BatchCallable {
    fun <T> execute(block: () -> T): T
}

fun <T: Any?> BatchCallable?.executeIfNotNull(block: () -> T): T {
    return if (this == null) {
        block()
    } else {
        execute(block)
    }
}
