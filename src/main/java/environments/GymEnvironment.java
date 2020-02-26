/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import communication.agent.AgentCommand;
import communication.system.Request;
import helperclasses.datastructures.Vec3;
import pathfinding.NavMeshContainer;
import pathfinding.Pathfinder;
import world.BeliefState;
import world.Observation;

/*
This is the Gym implementation of JsonEnvironment using SocketEnvironment.
It is used by Agents to send commands and receive observations
*/

public class GymEnvironment extends SocketEnvironment {

    public Pathfinder pathFinder;

    /**
     * Constructor
     */
    public GymEnvironment(EnvironmentConfig config) {
        super(config.host, config.port);
        // When this application has connected with the environment, an exchange in information takes place:
        // For now, this application sends nothing, and receives a navmesh of the world.
        NavMeshContainer navmesh = getResponse(Request.gymEnvironmentInitialisation(config));

        this.pathFinder = new Pathfinder(navmesh);
    }

    /**
     * This constructor is used whenever te game is already running and configured
     */
    private static EnvironmentConfig STANDARD_CONFIG = new EnvironmentConfig();
    public GymEnvironment() {
        super(STANDARD_CONFIG.host, STANDARD_CONFIG.port);
        // When this application has connected with the environment, an exchange in information takes place:
        // For now, this application sends nothing, and receives a navmesh of the world.
        NavMeshContainer navmesh = getResponse(Request.gymEnvironmentInitialisation(STANDARD_CONFIG));
        this.pathFinder = new Pathfinder(navmesh);
    }

    // Initialisation object

    private Observation getObservation(AgentCommand c){
        return getResponse(Request.command(c));
    }

    /**
     * This method will make the agent move a certain max distance toward the target
     *
     * @param target: The target the agent wants to move to
     * @param agentId: The ID of the agent (more precisely, the ID of the game-entity controlled by the agent)
     * @param agentPosition: The agent's current position
     * @return The observation following from the action
     */
    public Observation moveToward(String agentId, Vec3 agentPosition, Vec3 target) {
        return moveToward(agentId, agentPosition, target, false);
    }

    public Observation moveToward(String agentId, Vec3 agentPosition, Vec3 target, boolean jump) {
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
        return getObservation(AgentCommand.moveTowardCommand(agentId, targetDirection, jump));
    }

    /**
     * This will send a do-nothing command to unity, and return a new Observation.
     */
    public Observation observe(String agentId){
        return getObservation(AgentCommand.doNothing(agentId));
    }

    // send an interaction command to unity
    public Observation interactWith(String agentId, String target){
        return getObservation(AgentCommand.interactCommand(agentId, target));
    }

    // press play in Unity
    public Boolean startSimulation(){
        return getResponse(Request.startSimulation());
    }

    /**
     * this function updates the hazards in Unity, which is specified in EnvironmentConfig
     */
    public Boolean updateHazards(){
        return getResponse(Request.updateEnvironment());
    }
    // press pause in Unity
    public Boolean pauseSimulation(){
        return getResponse(Request.pauseSimulation());
    }
}
