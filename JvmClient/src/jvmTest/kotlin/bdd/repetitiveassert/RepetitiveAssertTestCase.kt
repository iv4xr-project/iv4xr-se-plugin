package bdd.repetitiveassert


import org.opentest4j.AssertionFailedError
import kotlin.reflect.KClass

interface RepetitiveAssertTestCase {
    val config: RepetitiveAssertConfig
}

suspend fun RepetitiveAssertTestCase.repeatUntilSuccess(
    repeats: Int = config.repeats,
    initialDelayMs: Long = config.initialDelayMs,
    delayMs: Long = config.delayMs,
    swallowedExceptionTypes: Set<KClass<out Throwable>> = setOf<KClass<out Throwable>>(
        AssertionError::class, AssertionFailedError::class
    ),
    assertBlock: suspend AssertBlockContext.() -> Unit
) {
    return repeatUntilSuccess(
        config = RepetitiveAssertConfig(
            repeats = repeats,
            initialDelayMs = initialDelayMs,
            delayMs = delayMs,
        ),
        swallowedExceptionTypes = swallowedExceptionTypes,
        assertBlock = assertBlock,
    )
}


