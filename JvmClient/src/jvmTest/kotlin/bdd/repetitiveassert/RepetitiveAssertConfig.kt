package bdd.repetitiveassert

const val defaultInitialDelayMs = 0L
const val defaultDelayMs = 100L
const val defaultRepeats = 2

data class RepetitiveAssertConfig(
    val initialDelayMs: Long = defaultInitialDelayMs,
    val delayMs: Long = defaultDelayMs,
    val repeats: Int = defaultRepeats,
)
