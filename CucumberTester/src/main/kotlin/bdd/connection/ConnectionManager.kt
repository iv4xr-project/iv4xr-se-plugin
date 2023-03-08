package bdd.connection

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.transport.ReconnectingSocketReaderWriter

class ConnectionManager(
    val config: Config,
    val connectionSetup: ConnectionSetup,
    val builder: JsonRpcSpaceEngineersBuilder = JvmSpaceEngineersBuilder.default(),
) : AutoCloseable {

    private suspend inline fun <T> ProcessWithConnection.watchForThrowables(block: suspend ()-> T): T {
        return try {
            block()
        } catch(e: Throwable) {
            throw ProcessException(this, e)
        }
    }

    var initiated: Boolean = false
        private set

    val connectionsById by lazy {
        connectionSetup.connections.associate {
            it.createId() to ProcessWithConnection(createConnection(it, factory = builder), it)
        }.apply {
            initiated = true
        }
    }

    val admin by lazy { connectionsById.getValue(connectionSetup.admin.createId()) }

    val mainClientCharacterId by lazy {
        mainClient.spaceEngineers.observer.observe().id
    }

    val mainClient by lazy {
        connectionsById.getValue(connectionSetup.mainClient.createId())
    }

    val observers by lazy { connectionSetup.observers.map { connectionsById.getValue(it.createId()) } }

    val clients by lazy {
        connectionSetup.clients.map { connectionsById.getValue(it.createId()) }
    }

    val nonMainClientGameObservers by lazy {
        connectionSetup.nonMainClientGameObservers.map { connectionsById.getValue(it.createId()) }
    }

    val games by lazy {
        connectionSetup.games.map { connectionsById.getValue(it.createId()) }
    }

    val all by lazy {
        connectionsById.values
    }

    suspend fun <T> mainClient(block: suspend ProcessWithConnection.() -> T): T = mainClient.watchForThrowables {
        block(mainClient)
    }

    suspend fun <T> admin(block: suspend ProcessWithConnection.() -> T): T = admin.watchForThrowables {
        block(admin)
    }

    private suspend fun <T> Collection<ProcessWithConnection>.parallelEach(block: suspend ProcessWithConnection.() -> T) =
        coroutineScope {
            this@parallelEach.map {
                async {
                    it.watchForThrowables {
                        block(it)
                    }
                }
            }.awaitAll()
        }

    suspend fun <T> clients(block: suspend ProcessWithConnection.() -> T) = clients.parallelEach(block)

    suspend fun <T> all(block: suspend ProcessWithConnection.() -> T) = all.parallelEach(block)

    suspend fun <T> nonMainClientGameObservers(block: suspend ProcessWithConnection.() -> T) =
        nonMainClientGameObservers.parallelEach(block)

    suspend fun <T> games(block: suspend ProcessWithConnection.() -> T): List<T> = games.parallelEach {
        try {
            spaceEngineers.admin.character.switch(spaceEngineers.admin.character.mainCharacterId())
        } catch (e: Exception) {
            /*
            We switch, but it's okay to fail - maybe sometimes we want to control the game and there is no character.
            Could use some refactoring to avoid this.
            */
        }
        block(this)
    }

    suspend fun <T> observers(block: suspend ProcessWithConnection.() -> T): List<T> = observers.parallelEach {
        try {
            mainClient.spaceEngineers.admin.character.localCharacterId()?.let {
                spaceEngineers.admin.character.switch(it)
            }
        } catch (e: Exception) {
            println("E: ${e.message}")
        }
        block(this)
    }

    override fun close() {
        if (initiated) {
            connectionsById.values.forEach { it.spaceEngineers.close() }
        }
    }

    companion object {
        fun createConnection(
            gameProcess: GameProcess,
            factory: JsonRpcSpaceEngineersBuilder
        ): ContextControllerWrapper {
            gameProcess.println("Connecting to plugin...")
            return ContextControllerWrapper(
                spaceEngineers = factory.fromStringLineReaderWriter(
                    agentId = gameProcess.createId(),
                    stringLineReaderWriter = ReconnectingSocketReaderWriter(
                        host = gameProcess.address,
                        port = gameProcess.pluginPort
                    )
                )
            ).apply {
                gameProcess.println(" connected")
            }
        }

    }
}
