package spaceEngineers.commands

class SeAgentCommand
private constructor(
    var agentId: String,
    var targetId: String,
    var cmd: SeAgentCommandType,
    var arg: Any
) {
    companion object {
        fun observe(agent: String): SeAgentCommand {
            return SeAgentCommand(agent, agent, SeAgentCommandType.OBSERVE, ObservationArgs())
        }

        fun observe(agent: String, observationArgs: ObservationArgs = ObservationArgs()): SeAgentCommand {
            return SeAgentCommand(agent, agent, SeAgentCommandType.OBSERVE, observationArgs)
        }

        fun moveAndRotate(agent: String, movementArgs: MovementArgs): SeAgentCommand {
            return SeAgentCommand(agent, agent, SeAgentCommandType.MOVE_ROTATE, movementArgs)
        }

        fun interact(agent: String, interactionArgs: InteractionArgs): SeAgentCommand {
            return SeAgentCommand(agent, agent, SeAgentCommandType.INTERACT, interactionArgs)
        }

        fun moveTowardCommand(agent: String, moveTowardsArgs: MoveTowardsArgs): SeAgentCommand {
            return SeAgentCommand(agent, agent, SeAgentCommandType.MOVETOWARD, moveTowardsArgs)
        }
    }
}