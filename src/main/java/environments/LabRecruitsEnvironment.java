/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import communication.agent.AgentCommand;
import communication.system.Request;
import helperclasses.datastructures.Vec3;
import pathfinding.Pathfinder;
import world.BeliefState;
import world.LabRecruitsRawNavMesh;
import world.LabWorldModel;
import world.LegacyObservation;

/**
 * An implementation of {@link nl.uu.cs.aplib.environments.JsonEnvironment}
 * using {@link environments.SocketEnvironment}. It facilitates the
 * communication between agents and the Lab Recruits game. A set of basic
 * methods to control the game is provided. Keep in mind that methods 
 * exposed by this Environment are very primitive; they do not apply any
 * form of reasoning. 
 */
public class LabRecruitsEnvironment extends SocketEnvironment {

	/**
	 * Navigation graph.
	 */
    public Pathfinder pathFinder;

    /**
     * A constructor to create an instance of this Environment. It expects an instance of
     * {@link game.LabRecruitsTestServer} to already running, and also an instance of  
     * the Lab Recruit game to be already launched. The given configuration object
     * contains information about the TCP port to be used to communicate with the game
     * instance. 
     * 
     * The constructor also asks the game to send the whole navigation-mesh of the level,
     * which it then stores in the {@link pathFinder} field.
     */
    public LabRecruitsEnvironment(LabRecruitsConfig config) {
        super(config.host, config.port);
        // When this application has connected with the environment, an exchange in information takes place:
        // For now, this application sends nothing, and receives a navmesh of the world.
        LabRecruitsRawNavMesh navmesh = getResponse(Request.gymEnvironmentInitialisation(config));

        this.pathFinder = new Pathfinder(navmesh);
    }

    private static LabRecruitsConfig STANDARD_CONFIG = new LabRecruitsConfig();

	/**
	 * To create an instance of this environment, using standard configuration; see
	 * {@link LabRecruitsConfig}.
	 */
    public LabRecruitsEnvironment() {
        super(STANDARD_CONFIG.host, STANDARD_CONFIG.port);
        // When this application has connected with the environment, an exchange in information takes place:
        // For now, this application sends nothing, and receives a navmesh of the world.
        LabRecruitsRawNavMesh navmesh = getResponse(Request.gymEnvironmentInitialisation(STANDARD_CONFIG));
        this.pathFinder = new Pathfinder(navmesh);
    }

    private LabWorldModel sendAgentCommand_andGetObservation(AgentCommand c){
    	LegacyObservation obs = getResponse(Request.command(c)); 
    	// covert the obtained observation to a WorldModel:
    	var wom = LegacyObservation.toWorldModel(obs) ;
        return wom ;
    }

    /**
     * This method will make the agent move a certain max distance toward the target.
     * Note that this method does not take obstacles into account. This is something
     * your agent needs to reason about by itself, based on the series of observations
     * it receives from this Environment.
     *
     * @param target: The target the agent wants to move to
     * @param agentId: The ID of the agent (more precisely, the ID of the game-entity controlled by the agent)
     * @param agentPosition: The agent's current position
     * @return The observation following from the action
     */
    public LabWorldModel moveToward(String agentId, Vec3 agentPosition, Vec3 target) {
        return moveToward(agentId, agentPosition, target, false);
    }

    // jumping is not supported for now
    private LabWorldModel moveToward(String agentId, Vec3 agentPosition, Vec3 target, boolean jump) {
        //define the max distance the agent wants to move ahead between updates
        float maxDist = 2f;

        //Calculate where the agent wants to move to
        Vec3 targetDirection = Vec3.subtract(target, agentPosition);
        targetDirection.normalize();

        //Check if we can move the full distance ahead
        double dist = target.distance(agentPosition);
        if (dist < maxDist) {
            targetDirection.multiply(dist);
        } else {
            targetDirection.multiply(maxDist);
        }
        //add the agent own position to the current coordinates
        targetDirection.add(agentPosition);

        //send the command
        return sendAgentCommand_andGetObservation(AgentCommand.moveTowardCommand(agentId, targetDirection, jump));
    }

    /**
     * This will send a do-nothing command to unity, and return a new Observation.
     */
    public LabWorldModel observe(String agentId){
        return sendAgentCommand_andGetObservation(AgentCommand.doNothing(agentId));
    }

    // send an interaction command to unity
    public LabWorldModel interactWith(String agentId, String target){
        return sendAgentCommand_andGetObservation(AgentCommand.interactCommand(agentId, target));
    }

    /**
     * Press the "play-button" in Unity. If left unpressed, no simulation/game-play can start.
     */
    public Boolean startSimulation(){
        return getResponse(Request.startSimulation());
    }

    /**
     * Press the "pause-button" in Unity. This will pull the Unity-side paused.
     */
    public Boolean pauseSimulation(){
        return getResponse(Request.pauseSimulation());
    }
    
    /**
     * this function updates the hazards in Unity, which is specified in EnvironmentConfig.
     * Currently broken :| TODO.
     */
    public Boolean updateHazards(){
        return getResponse(Request.updateEnvironment());
    }

}
