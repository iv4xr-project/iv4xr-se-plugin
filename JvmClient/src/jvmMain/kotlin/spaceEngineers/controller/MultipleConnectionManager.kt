package spaceEngineers.controller

import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.closeIfCloseable

class MultipleConnectionManager(
    val connections: MutableMap<String, SpaceEngineers> = mutableMapOf(),
    val characterIdByName: MutableMap<String, String> = mutableMapOf(),
): AutoCloseable {

    val server: SpaceEngineers
        get() = connections[SERVER] ?: error("No server connection, call setServer first")

    fun connection(name: String, block: SpaceEngineers.() -> Unit) {
        block(connection(name))
    }

    val clients: Map<String, SpaceEngineers>
        get() = connections.filter { it.key != SERVER }.toMap()

    fun server(block: SpaceEngineers.() -> Unit) {
        block(connection(SERVER))
    }

    fun connection(name: String): SpaceEngineers {
        return connections[name] ?: error("No connection of name $name")
    }

    fun getCharacterIdByName(name: String): String {
        return characterIdByName.getOrPut(name) {
            connection(name).observer.observe().id
        }
    }

    fun addClient(name: String, address: String, port: Int): SpaceEngineers {
        check(name !in connections) {
            "$name already in connections!"
        }
        val se = ContextControllerWrapper(
            spaceEngineers = JsonRpcSpaceEngineersBuilder.fromStringLineReaderWriter(
                agentId = name,
                stringLineReaderWriter = SocketReaderWriter(host = address, port = port)
            )
        )
        connections[name] = se
        return se
    }

    override fun close() {
        connections.values.forEach {
            it.closeIfCloseable()
        }
    }

    fun setServer(address: String, port: Int): SpaceEngineers {
        return addClient(name = SERVER, address, port)
    }


    companion object {
        const val SERVER = "SERVER"
    }
}
