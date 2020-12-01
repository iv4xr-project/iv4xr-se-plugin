package spaceEngineers.commands;

import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;

public class SeAgentCommand {
    public SeAgentCommandType cmd;
    public String agentId;  // Not used.
    public String targetId;  // Not used.
    public Object arg;

    /**
     * The user is not permitted to create new agent requests
     */
    private SeAgentCommand(String agentId, String targetId, SeAgentCommandType cmd, Object arg) {

        this.agentId = agentId;
        this.targetId = targetId;
        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * The agent does not do anything. This can be used to just observe the gym.
     */
    public static SeAgentCommand doNothing(String agent) {
        return new SeAgentCommand(agent, agent, SeAgentCommandType.OBSERVE, null);
    }

    /**
     * Moves an agent in a certain direction without using a jump
     */
    public static SeAgentCommand moveTowardCommand(String agent, Vec3 direction) {
        return moveTowardCommand(agent, direction, false);
    }

    public static SeAgentCommand moveAndRotate(String agent, MovementArgs movementArgs) {
        return new SeAgentCommand(agent, agent, SeAgentCommandType.MOVE_ROTATE, movementArgs);
    }

    public static SeAgentCommand moveAndRotate(String agent, Vec3 movement, Vec3 rotation, float roll) {
        return moveAndRotate(agent, new MovementArgs(movement, rotation, roll));
    }

    public static SeAgentCommand interact(String agent, int slot) {
        return new SeAgentCommand(agent, agent, SeAgentCommandType.INTERACT, slot);
    }


    /**
     * Moves an agent in a certain direction.
     */
    public static SeAgentCommand moveTowardCommand(String agent, Vec3 direction, boolean jump) {
        return new SeAgentCommand(agent, agent, SeAgentCommandType.MOVETOWARD, new Tuple<>(direction, jump));
    }
}
