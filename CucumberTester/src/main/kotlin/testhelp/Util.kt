package testhelp

import kotlinx.coroutines.runBlocking
import java.lang.reflect.UndeclaredThrowableException

fun <R> hideUndeclaredThrowableException(block: suspend () -> R) = runBlocking {
    try {
        block()
    } catch (e: UndeclaredThrowableException) {
        throw e.cause ?: e
    }
}
