package bdd.setup

import bdd.globalCm
import kotlinx.coroutines.delay
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.connection.ConnectionManager
import testhelp.hideUndeclaredThrowableException


interface ConnectionManagerUser {
    val connectionManager: ConnectionManager
    fun observers(block: suspend ContextControllerWrapper.() -> Unit): List<Unit>
    fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T
    fun nonMainClientGameObservers(block: suspend ContextControllerWrapper.() -> Unit): List<Unit>
    fun clients(block: suspend ContextControllerWrapper.() -> Unit): List<Unit>
    fun games(block: suspend ContextControllerWrapper.() -> Unit): List<Unit>
    fun admin(block: suspend ContextControllerWrapper.() -> Unit)

    suspend fun smallPause()

    suspend fun pause()

    suspend fun bigPause()
}

class GlobalConnectionManagerUser() : ConnectionManagerUser {

    override val connectionManager: ConnectionManager
        get() = globalCm

    override fun observers(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.observers(block)
    }

    override fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T = hideUndeclaredThrowableException {
        connectionManager.mainClient(block)
    }

    override fun nonMainClientGameObservers(block: suspend ContextControllerWrapper.() -> Unit) =
        hideUndeclaredThrowableException {
            connectionManager.nonMainClientGameObservers(block)
        }


    override fun clients(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.clients(block)
    }

    override fun games(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.games(block)
    }

    override fun admin(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.admin(block)
    }

    override suspend fun smallPause() {
        delay(500)
    }

    override suspend fun pause() {
        delay(5_000)
    }

    override suspend fun bigPause() {
        delay(15_000)
    }
}



class RealConnectionManagerUser(override val connectionManager: ConnectionManager) : ConnectionManagerUser {

    override fun observers(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.observers(block)
    }

    override fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T = hideUndeclaredThrowableException {
        connectionManager.mainClient(block)
    }

    override fun nonMainClientGameObservers(block: suspend ContextControllerWrapper.() -> Unit) =
        hideUndeclaredThrowableException {
            connectionManager.nonMainClientGameObservers(block)
        }


    override fun clients(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.clients(block)
    }

    override fun games(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.games(block)
    }

    override fun admin(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        connectionManager.admin(block)
    }

    override suspend fun smallPause() {
        delay(500)
    }

    override suspend fun pause() {
        delay(5_000)
    }

    override suspend fun bigPause() {
        delay(15_000)
    }
}

