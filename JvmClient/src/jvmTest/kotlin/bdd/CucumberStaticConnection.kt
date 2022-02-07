package bdd

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.closeIfCloseable
import testhelp.TEST_AGENT

object CucumberStaticConnection: AutoCloseable {
    private const val LOCK = "LOCK"

    var environment: ContextControllerWrapper? = null

    fun connect() {
        close()
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

    override fun close() {
        environment?.spaceEngineers.closeIfCloseable()
        environment.closeIfCloseable()
        environment = null
    }
}
