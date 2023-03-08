package bdd.setup.process

import bdd.connection.ConnectionSetup
import bdd.connection.GameProcess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <In, Out> Collection<In>.parallelEach(block: suspend (In) -> Out): Collection<Out> =
    coroutineScope {
        this@parallelEach.map {
            async {
                block(it)
            }
        }.awaitAll()
    }

class AllGamesManager(
    val connectionSetup: ConnectionSetup,
    val username: String,
    val password: String,
    val psExec: String,
) {

    suspend fun ensureReady() {
        connectionSetup.connections.parallelEach { gameProcess ->
            val gpm = GameProcessManager(gameProcess.toRemoteProcessExecutor(), gameProcess)
            gpm.ensureGameIsReadyForTesting()
            gpm.close()
        }
    }

    suspend fun kill() {
        connectionSetup.connections.parallelEach { gameProcess ->
            val gpm = GameProcessManager(gameProcess.toRemoteProcessExecutor(), gameProcess)
            gpm.kill()
            gpm.close()
        }
    }

    private fun GameProcess.toRemoteProcessExecutor(): RemoteProcessExecutor {
        return RemoteProcessExecutor(
            hostname = address,
            username = username,
            password = password,
            psExec = psExec,
        )
    }
}
