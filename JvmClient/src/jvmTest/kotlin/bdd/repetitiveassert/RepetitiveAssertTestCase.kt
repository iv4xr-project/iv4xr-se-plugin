package bdd.repetitiveassert

interface RepetitiveAssertTestCase {
    val config: RepetitiveAssertConfig
}

suspend fun RepetitiveAssertTestCase.repeatUntilSuccess(
    repeats: Int = config.repeats,
    initialDelayMs: Long = config.initialDelayMs,
    delayMs: Long = config.delayMs,
    assertBlock: AssertBlockContext.() -> Unit
) {
    return repeatUntilSuccess(
        config = RepetitiveAssertConfig(
            repeats = repeats,
            initialDelayMs = initialDelayMs,
            delayMs = delayMs,
        ),
        assertBlock = assertBlock,
    )
}


