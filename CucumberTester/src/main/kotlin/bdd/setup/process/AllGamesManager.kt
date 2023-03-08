package bdd.setup.process

import bdd.connection.ConnectionSetup
import bdd.connection.GameProcess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.net.DatagramSocket;
import java.net.InetAddress;


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

    fun isIpLocal(ip: String): Boolean {
        return DatagramSocket().use { socket ->
            socket.connect(InetAddress.getByName(ip), 10002)
            val localAddress = socket.localAddress.hostAddress
            println("ip: $ip local ${localAddress}")
            ip == localAddress
        }
    }

    private fun GameProcess.toExecutor(): ProcessExecutor {
        return if (isLocal()) {
            this.println("local")
            toLocalProcessExecutor()
        } else {
            this.println("remote")
            toRemoteProcessExecutor()
        }
    }

    private fun GameProcess.isLocal(): Boolean {
        return isIpLocal(address)
    }

    suspend fun ensureReady() {
        connectionSetup.connections.parallelEach { gameProcess ->
            val gpm = GameProcessManager(gameProcess.toExecutor(), gameProcess)
            gpm.ensureGameIsReadyForTesting()
            gpm.close()
        }
    }

    suspend fun kill() {
        connectionSetup.connections.parallelEach { gameProcess ->
            val gpm = GameProcessManager(gameProcess.toExecutor(), gameProcess)
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

    private fun GameProcess.toLocalProcessExecutor(): LocalProcessExecutor {
        return LocalProcessExecutor(
        )
    }
}
