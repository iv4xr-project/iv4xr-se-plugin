package bdd.repetitiveassert

import kotlinx.coroutines.delay

suspend fun repeatUntilSuccess(
    config: RepetitiveAssertConfig,
    assertBlock: AssertBlockContext.() -> Unit
) {

    delay(config.initialDelayMs)
    repeat(config.repeats) {
        try {
            AssertBlockContext(config, it).assertBlock()
            return
        } catch (e: AssertionError) {
            //swallow
        }
        delay(config.delayMs)
    }
    AssertBlockContext(config, config.repeats).assertBlock()
}


