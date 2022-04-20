package bdd

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager

abstract class AbstractMultiplayerSteps(

) : AutoCloseable {

    val cm: ConnectionManager
        get() = CM

    override fun close() {
        cm.close()
    }

    suspend fun smallPause() {
        delay(500)
    }

    suspend fun pause() {
        delay(5_000)
    }

    suspend fun bigPause() {
        delay(15_000)
    }

    fun observers(block: suspend ContextControllerWrapper.() -> Unit) = runBlocking {
        cm.observers(block)
    }

    fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T = runBlocking {
        cm.mainClient(block)
    }

    fun clients(block: suspend ContextControllerWrapper.() -> Unit) = runBlocking {
        cm.clients(block)
    }

    fun games(block: suspend ContextControllerWrapper.() -> Unit) = runBlocking {
        cm.games(block)
    }

    fun admin(block: suspend ContextControllerWrapper.() -> Unit) = runBlocking {
        cm.admin(block)
    }

    companion object {
        lateinit var CM: ConnectionManager
    }
}
