/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import eu.iv4xr.framework.spatial.Vec3 ;
import helperclasses.datastructures.LegacyTuple;

/**
 * Part of the wrapper class to wrap commands sent to Lab Recruits. See also the class
 * Request.
 * WP: This is a bit legacy, let's not change this.
 * 
 * 
 * The AgentRequest class is a Request that always returns an observation of the gym.
 * @author Maurin
 */
public class AgentCommand {
	
	public enum AgentCommandType
	{
	    DONOTHING,
	    MOVETOWARD,
	    INTERACT
	}

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
     * Moves an agent towards the given location, without using a jump
     */
    public static AgentCommand moveTowardCommand(String agent, Vec3 targetLocation) {
        return moveTowardCommand(agent, targetLocation, false);
    }

    /**
     * Moves an agent towards the given location.
     */
    public static AgentCommand moveTowardCommand(String agent, Vec3 targetLocation, boolean jump) {
    	// use LegacyTuple here. Dont cange it to e.g. Pair, as the serialization expect specific
    	// field-names:
        return new AgentCommand(agent, agent, AgentCommandType.MOVETOWARD, new LegacyTuple<>(targetLocation, jump));
    }

    /**
     * Lets an agent interact with an object in the gym.
     */
    public static AgentCommand interactCommand(String agent, String target) {
        return new AgentCommand(agent, target, AgentCommandType.INTERACT, null);
    }
}
