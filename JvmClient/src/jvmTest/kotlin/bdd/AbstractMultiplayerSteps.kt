package bdd

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager

abstract class AbstractMultiplayerSteps(

) : AutoCloseable {

    //var mcm: MCM = MCM(ConnectionSetup.SINGLE_COMPUTER_DEDICATED)
    lateinit var cm: ConnectionManager

    override fun close() {
        cm.close()
    }

    suspend fun smallPause() {
        delay(500)
    }

    suspend fun pause() {
        delay(5_000)
    }

    suspend fun bigPause() {
        delay(15_000)
    }

    fun observers(block: suspend SpaceEngineers.() -> Unit) = runBlocking {
        smallPause()
        cm.observers(block)
        smallPause()
    }

    fun mainClient(block: suspend SpaceEngineers.() -> Unit) = runBlocking {
        cm.mainClient(block)
        smallPause()
    }

    fun clients(block: suspend SpaceEngineers.() -> Unit) = runBlocking {
        cm.clients(block)
    }

    fun admin(block: suspend SpaceEngineers.() -> Unit) = runBlocking {
        cm.admin(block)
    }


}
