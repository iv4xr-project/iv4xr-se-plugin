package spaceEngineers.controller.connection

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

    val characterId by lazy {
        mainClient.spaceEngineers.observer.observe().id
    }

    val mainClient by lazy {
        connectionsById.getValue(connectionSetup.mainClient.createId())
    }

    val observers by lazy { connectionSetup.observers.map { connectionsById.getValue(it.createId()) } }

    val clients by lazy {
        connectionSetup.clients.map { connectionsById.getValue(it.createId()) }
    }

    suspend fun mainClient(block: suspend ContextControllerWrapper.() -> Unit) {
        block(mainClient.spaceEngineers)
    }

    suspend fun admin(block: suspend ContextControllerWrapper.() -> Unit) {
        block(admin.spaceEngineers)
    }

    suspend fun clients(block: suspend ContextControllerWrapper.() -> Unit) {
        coroutineScope {
            clients.map {
                async {
                    block(it.spaceEngineers)
                }
            }.awaitAll()
        }
    }

    suspend fun observers(block: suspend ContextControllerWrapper.() -> Unit) {
        observers.forEach {
            it.spaceEngineers.admin.character.switch(characterId)
            block(it.spaceEngineers)
        }
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
