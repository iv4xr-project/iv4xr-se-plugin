package bdd.repetitiveassert

import kotlinx.coroutines.delay
import kotlin.reflect.KClass

suspend fun repeatUntilSuccess(
    config: RepetitiveAssertConfig,
    swallowedExceptionTypes: Set<KClass<out Throwable>> = setOf<KClass<out Throwable>>(
        AssertionError::class,
    ),
    assertBlock: suspend AssertBlockContext.() -> Unit
) {

    delay(config.initialDelayMs)
    repeat(config.repeats) {
        try {
            AssertBlockContext(config, it).assertBlock()
            return
        } catch (e: Throwable) {
            if (e::class in swallowedExceptionTypes) {
                //swallow
                println(e.message)
            } else {
                throw e
            }
        }
        delay(config.delayMs)
    }
    AssertBlockContext(config, config.repeats).assertBlock()
}


