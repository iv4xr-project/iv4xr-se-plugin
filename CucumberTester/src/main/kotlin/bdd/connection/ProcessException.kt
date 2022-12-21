package bdd.connection

class ProcessException(
    val processWithConnection: ProcessWithConnection,
    source: Throwable,
) : Exception(source.message + " (${processWithConnection.gameProcess.simpleString()})", source)
