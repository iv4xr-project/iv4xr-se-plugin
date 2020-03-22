/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package communication.system;

import communication.agent.AgentCommand;
import environments.EnvironmentConfig;
import pathfinding.NavMeshContainer;
import world.LegacyObservation;

/**
 * because it already contains an invoker and target which will be useful in multi agent environments
 * This Request class provides a little bit more utility that handles the casting for you for the ResponseType
 * @author Maurin
 */

public class Request<ResponseType>  {

    /**
     * Java can not determine the class of ResponseType at runtime.
     * In this case, storing an instance of Class<ResponseType> to cast the response object is seen as good practice.
     */
    public transient final Class<ResponseType> responseType;

    public RequestType cmd;
    public Object arg;

    /**
     * This constructor is based on the sendCommand method from JsonEnvironment
     */
    private Request(Class<ResponseType> responseType, RequestType cmd, Object arg) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * This constructor is based on the sendCommand method from JsonEnvironment
     */
    private Request(Class<ResponseType> responseType, RequestType cmd) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = cmd;
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
    public static Request<NavMeshContainer> gymEnvironmentInitialisation(EnvironmentConfig config) {
        return new Request<>(NavMeshContainer.class, RequestType.INIT, config);
    }

    /**
     * Request an observation after executing the sent Command
     */
    public static Request<LegacyObservation> command(AgentCommand command) {
        return new Request<>(LegacyObservation.class, RequestType.AGENTCOMMAND, command);
    }

    /**
     * Request an update on the hazards
     */
    public static Request<Boolean> updateEnvironment(){
        return new Request<>(Boolean.class, RequestType.UPDATE_ENVIRONMENT);
    }
}
