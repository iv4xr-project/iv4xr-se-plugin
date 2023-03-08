package bdd.setup.process

interface ProcessExecutor {
    fun execute(cmd: List<String>): String
    fun startGuiApp(cmd: List<String>): String
}
