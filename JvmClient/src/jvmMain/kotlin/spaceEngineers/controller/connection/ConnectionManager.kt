package spaceEngineers.controller.connection

import kotlinx.coroutines.*
import spaceEngineers.controller.*
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.closeIfCloseable

class ConnectionManager(
    val connectionSetup: ConnectionSetup,
    val builder: JsonRpcSpaceEngineersBuilder = JvmSpaceEngineersBuilder.default(),
) : AutoCloseable {

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

    val games by lazy {
        connectionSetup.games.map { connectionsById.getValue(it.createId()) }
    }

    suspend fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T {
        return block(mainClient.spaceEngineers)
    }

    suspend fun admin(block: suspend ContextControllerWrapper.() -> Unit) {
        block(admin.spaceEngineers)
    }

    private suspend fun List<ProcessWithConnection>.parallelEach(block: suspend ContextControllerWrapper.() -> Unit) =
        coroutineScope {
            this@parallelEach.map {
                async {
                    block(it.spaceEngineers)
                }
            }.awaitAll()
        }

    suspend fun clients(block: suspend ContextControllerWrapper.() -> Unit) = clients.parallelEach(block)

    suspend fun games(block: suspend ContextControllerWrapper.() -> Unit) = games.parallelEach(block)

    suspend fun observers(block: suspend ContextControllerWrapper.() -> Unit) = observers.parallelEach {
        spaceEngineers.admin.character.switch(mainClientCharacterId)
        block(this)
    }

    private fun createConnection(
        gameProcess: GameProcess,
        factory: JsonRpcSpaceEngineersBuilder
    ): ContextControllerWrapper {
        print("Connecting to plugin ${gameProcess.address}:${gameProcess.pluginPort}...")
        return ContextControllerWrapper(
            spaceEngineers = factory.fromStringLineReaderWriter(
                agentId = gameProcess.createId(),
                stringLineReaderWriter = SocketReaderWriter(
                    host = gameProcess.address,
                    port = gameProcess.pluginPort.toInt()
                )
            )
        ).apply {
            println(" connected")
        }
    }

    override fun close() {
        if (initiated) {
            connectionsById.values.forEach { it.spaceEngineers.closeIfCloseable() }
        }
    }

}
