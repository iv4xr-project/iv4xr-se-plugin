/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import world.LabRecruitsRawNavMesh;
import world.Observation;

/**
 * A wrapper for commands to be sent to the Lab Recruits. 
 * WP: This is a bit legacy, let's not change this.
 * 
 * Because it already contains an invoker and target which will be useful in multi agent environments
 * This Request class provides a little bit more utility that handles the casting for you for the ResponseType
 * @author Maurin
 */

public class Request<ResponseType>  {
	
	
	/**
	 * Keep this enum synced with Unity!
	 */
	public enum RequestType
	{
	    DISCONNECT,
	    PAUSE,
	    START,
	    INIT,
	    UPDATE_ENVIRONMENT,
	    AGENTCOMMAND
	}

    /**
     * Java can not determine the class of ResponseType at runtime.
     * In this case, storing an instance of Class<ResponseType> to cast the response object is seen as good practice.
     */
    public transient final Class<ResponseType> responseType;

    /**
     * Specifying the type of the request to Lab Recruits, e.g. to initialize the game,
     * or to send a command for an agent.
     * 
     * The name of the field, "cmd", is a bit misleading, because it does not hold the
     * actual command for Lab Recruits agent, but rather just representing the request
     * type. The actual command, if there is one, is embedded in the "arg" field.
     */
    public RequestType cmd ;
    
    /**
     * The argument of the request. E.g. for INIT-request it will be a configuration parameters
     * for Lab Recruits. For Agent-command request it will be the command name and parameters.
     */
    public Object arg;

    /**
     * This constructor is based on the sendCommand method from JsonEnvironment
     */
    private Request(Class<ResponseType> responseType, RequestType reqType, Object arg) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = reqType;
        this.arg = arg;
    }

    /**
     * This constructor is based on the sendCommand method from JsonEnvironment
     */
    private Request(Class<ResponseType> responseType, RequestType reqType) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = reqType;
        this.arg = null;
    }

    /**
     * Disconnect from Unity
     * @return succes
     */
    public static Request<Boolean> disconnect() {
        return new Request<>(Boolean.class, RequestType.DISCONNECT);
    }

    /**
     * Press Pause in unity
     * @return succes
     */
    public static Request<Boolean> pauseSimulation() {
        return new Request<>(Boolean.class, RequestType.PAUSE);
    }

    /**
     * Press play in Unity
     * @return succes
     */
    public static Request<Boolean> startSimulation() {
        return new Request<>(Boolean.class, RequestType.START);
    }

    /**
     * Request the initialisation data from Unity:
     * - Navmesh
     * - Agent ID's along with their implementation type
     * @return GymInitialisation
     */
    public static Request<LabRecruitsRawNavMesh> gymEnvironmentInitialisation(LabRecruitsConfig config) {
        return new Request<>(LabRecruitsRawNavMesh.class, RequestType.INIT, config);
    }

    /**
     * Request an observation after executing the sent Command
     */
    public static Request<Observation> command(AgentCommand command) {
        return new Request<>(Observation.class, RequestType.AGENTCOMMAND, command);
    }

    /**
     * Request an update on the hazards
     */
    public static Request<Boolean> updateEnvironment(){
        return new Request<>(Boolean.class, RequestType.UPDATE_ENVIRONMENT);
    }
}
