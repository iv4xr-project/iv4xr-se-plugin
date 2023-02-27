package bdd.setup.process

import java.io.File

object SECommands {
    const val EXECUTABLE = "SpaceEngineers.exe"
    const val EXECUTABLE_DEDICATED = "SpaceEngineersDedicated.exe"
    const val NO_TASKS_RUNNING = "INFO: No tasks are running which match the specified criteria."
    const val EXECUTABLE_FULL_DEFAULT_PATH = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\SpaceEngineers\\Bin64\\SpaceEngineers.exe"

    fun killCmd(executable: String = EXECUTABLE): List<String> {
        return listOf("""C:\windows\system32/taskkill""", "/IM", executable, "/F")
    }

    fun isRunningCmd(executable: String = EXECUTABLE, fo: String = "list"): List<String> {
        return listOf("tasklist", "/fi", """"ImageName eq $executable"""", "/fo", fo)
    }

    fun remoteExecutionTemplate(cmd: String, hostname: String, username: String, password: String): String {
        return """
    
${'$'}User = "$username"
${'$'}Password = ConvertTo-SecureString -String "$password" -AsPlainText -Force
${'$'}Credential = [pscredential]::new(${'$'}User,${'$'}Password)

Invoke-Command -ComputerName $hostname -ScriptBlock { $cmd } -credential ${'$'}Credential
""".trimIndent()
    }

    fun executeRemoteCommand(hostname: String, username: String, password: String, cmd: List<String>): String {
        val template = remoteExecutionTemplate(
            cmd = cmd.joinToString(" "),
            hostname = hostname,
            username = username,
            password = password,
        )
        val file = File.createTempFile("powershell", ".ps1")
        file.writeText(template)
        return executeLocalCommand(
            listOf(
                "Powershell.exe",
                "-ExecutionPolicy",
                "Bypass",
                "-File",
                file.absolutePath
            )
        )
    }

    fun executeLocalCommand(cmd: List<String>): String {
        val process =
            ProcessBuilder(*cmd.toTypedArray())
                .redirectErrorStream(true)
                .start()
        process.waitFor()
        return process.inputStream.reader().readText()
    }
}
