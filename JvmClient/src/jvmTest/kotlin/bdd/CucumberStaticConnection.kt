package bdd

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.transport.closeIfCloseable
import testhelp.TEST_AGENT

object CucumberStaticConnection {
    private const val LOCK = "LOCK"

    var environment: ContextControllerWrapper? = null

    fun connect() {
        disconnect()
        environment = ContextControllerWrapper(
            spaceEngineers = JsonRpcSpaceEngineersBuilder.localhost(agentId = TEST_AGENT)
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
