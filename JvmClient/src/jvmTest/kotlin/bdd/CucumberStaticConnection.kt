package bdd

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.closeIfCloseable
import testhelp.TEST_AGENT
import java.net.Socket

object CucumberStaticConnection {
    private const val LOCK = "LOCK"

    var environment: ContextControllerWrapper? = null

    fun connect() {
        disconnect()
        environment = ContextControllerWrapper(
            spaceEngineers = JsonRpcSpaceEngineersBuilder.fromStringLineReaderWriter(
                agentId = TEST_AGENT,
                stringLineReaderWriter = AlwaysReturnSameLineReaderWriter("")
            )
        )
    }

    fun get(): ContextControllerWrapper = synchronized(LOCK) {
        if (environment == null) {
            connect()
        }
        return environment!!
    }

    fun disconnect() {
        environment?.spaceEngineers.closeIfCloseable()
        environment.closeIfCloseable()
        environment = null
    }
}
