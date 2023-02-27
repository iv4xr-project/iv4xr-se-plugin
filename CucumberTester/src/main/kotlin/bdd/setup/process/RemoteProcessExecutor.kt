package bdd.setup.process

import bdd.setup.process.SECommands.executeLocalCommand
import bdd.setup.process.SECommands.executeRemoteCommand

class RemoteProcessExecutor(
    val hostname: String,
    val psExec: String,
    val username: String,
    val password: String,
) : ProcessExecutor {
    override fun execute(cmd: List<String>): String {
        return executeRemoteCommand(hostname, username = username, password = password, cmd = cmd)
    }

    override fun startGuiApp(cmd: List<String>): String {
        return executeLocalCommand(
            listOf(
                psExec,
                "-i",
                "2",
                "-accepteula",
                "-u",
                username,
                "-p",
                password,
                "\\\\$hostname",
                """"${cmd.first()}""""
            )
        )
    }
}
