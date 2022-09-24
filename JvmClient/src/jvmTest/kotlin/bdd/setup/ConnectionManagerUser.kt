package bdd.setup

import bdd.globalCm
import kotlinx.coroutines.delay
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ProcessWithConnection
import testhelp.hideUndeclaredThrowableException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

interface Pauseable {
    suspend fun smallPause()

    suspend fun pause()

    suspend fun bigPause()

}

class DefaultPauseable(
    private val smallPauseDuration: Duration = 500.milliseconds,
    private val pauseDuration: Duration = 5.seconds,
    private val bigPauseDuration: Duration = 15.seconds,
) : Pauseable {
    override suspend fun smallPause() {
        delay(smallPauseDuration)
    }

    override suspend fun pause() {
        delay(pauseDuration)
    }

    override suspend fun bigPause() {
        delay(bigPauseDuration)
    }

}


interface ConnectionManagerUser: Pauseable {
    val connectionManager: ConnectionManager
    fun <T> observers(block: suspend ProcessWithConnection.() -> T): List<T>
    fun <T> mainClient(block: suspend ProcessWithConnection.() -> T): T
    fun <T> nonMainClientGameObservers(block: suspend ProcessWithConnection.() -> T): List<T>
    fun <T> clients(block: suspend ProcessWithConnection.() -> T): List<T>
    fun <T> all(block: suspend ProcessWithConnection.() -> T): List<T>
    fun <T> games(block: suspend ProcessWithConnection.() -> T): List<T>
    fun <T> admin(block: suspend ProcessWithConnection.() -> T): T


}

class GlobalConnectionManagerUser(pauseable: Pauseable = DefaultPauseable()) : ConnectionManagerUser,
    Pauseable by pauseable {

    override val connectionManager: ConnectionManager
        get() = globalCm

    override fun <T> observers(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.observers(block)
    }

    override fun <T> mainClient(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.mainClient(block)
    }

    override fun <T> nonMainClientGameObservers(block: suspend ProcessWithConnection.() -> T) =
        hideUndeclaredThrowableException {
            connectionManager.nonMainClientGameObservers(block)
        }


    override fun <T> clients(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.clients(block)
    }

    override fun <T> all(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.all(block)
    }

    override fun <T> games(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.games(block)
    }

    override fun <T> admin(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
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


class RealConnectionManagerUser(
    override val connectionManager: ConnectionManager,
    pauseable: Pauseable = DefaultPauseable()
) : ConnectionManagerUser, Pauseable by pauseable {

    override fun <T> observers(block: suspend ProcessWithConnection.() -> T) =
        hideUndeclaredThrowableException {
            connectionManager.observers(block)
        }

    override fun <T> mainClient(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.mainClient(block)
    }

    override fun <T> nonMainClientGameObservers(block: suspend ProcessWithConnection.() -> T) =
        hideUndeclaredThrowableException {
            connectionManager.nonMainClientGameObservers(block)
        }

    override fun <T> clients(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.clients(block)
    }

    override fun <T> all(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.all(block)
    }

    override fun <T> games(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.games(block)
    }

    override fun <T> admin(block: suspend ProcessWithConnection.() -> T) = hideUndeclaredThrowableException {
        connectionManager.admin(block)
    }

}

