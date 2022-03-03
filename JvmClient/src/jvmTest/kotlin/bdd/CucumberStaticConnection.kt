package bdd

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.transport.closeIfCloseable
import testhelp.TEST_AGENT

object CucumberStaticConnection: AutoCloseable {
    private const val LOCK = "LOCK"

    var environment: ContextControllerWrapper? = null

    val spaceEngineersBuilder = JvmSpaceEngineersBuilder.default()

    fun connect() {
        close()
        environment = ContextControllerWrapper(
            spaceEngineers = spaceEngineersBuilder.localhost(
                agentId = TEST_AGENT,
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
