/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package communication.agent;

import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;

/**
 * The AgentRequest class is a Request that always returns an observation of the gym.
 * @author Maurin
 */
public class AgentCommand {

    public AgentCommandType cmd;
    public String agentId;
    public String targetId;
    public Object arg;

    /**
     * The user is not permitted to create new agent requests
     */
    private AgentCommand(String agentId, String targetId, AgentCommandType cmd, Object arg) {

        this.agentId = agentId;
        this.targetId = targetId;
        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * The agent does not do anything. This can be used to just observe the gym.
     */
    public static AgentCommand doNothing(String agent) {
        return new AgentCommand(agent, agent, AgentCommandType.DONOTHING, null);
    }

    /**
     * Moves an agent in a certain direction without using a jump
     */
    public static AgentCommand moveTowardCommand(String agent, Vec3 direction) {
        return moveTowardCommand(agent, direction, false);
    }

    /**
     * Moves an agent in a certain direction.
     */
    public static AgentCommand moveTowardCommand(String agent, Vec3 direction, boolean jump) {
        return new AgentCommand(agent, agent, AgentCommandType.MOVETOWARD, new Tuple<>(direction, jump));
    }

    /**
     * Lets an agent interact with an object in the gym.
     */
    public static AgentCommand interactCommand(String agent, String target) {
        return new AgentCommand(agent, target, AgentCommandType.INTERACT, null);
    }
}
