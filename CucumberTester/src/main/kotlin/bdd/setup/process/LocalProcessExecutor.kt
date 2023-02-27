package bdd.setup.process

import bdd.setup.process.SECommands.executeLocalCommand

class LocalProcessExecutor : ProcessExecutor {
    override fun execute(cmd: List<String>): String {
        return executeLocalCommand(cmd)
    }

    override fun startGuiApp(cmd: List<String>): String {
        return executeLocalCommand(cmd)
    }
}
