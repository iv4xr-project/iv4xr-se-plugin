package spaceEngineers.commands

class SeSessionCommand private constructor(  // TODO(PP): Add request type enum. So far only load command is supported.
    var scenarioPath: String
) {
    companion object {
        fun load(scenarioPath: String): SeSessionCommand {
            return SeSessionCommand(scenarioPath)
        }
    }
}