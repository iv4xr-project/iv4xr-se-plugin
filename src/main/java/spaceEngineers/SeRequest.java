package spaceEngineers;

import communication.agent.AgentCommand;
import communication.system.Request;
import communication.system.RequestType;
import world.LegacyObservation;

public class SeRequest<ResponseType> {

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
    private SeRequest(Class<ResponseType> responseType, RequestType cmd, Object arg) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * This constructor is based on the sendCommand method from JsonEnvironment
     */
    private SeRequest(Class<ResponseType> responseType, RequestType cmd) {
        // convert he command to string
        this.responseType = responseType;

        this.cmd = cmd;
        this.arg = null;
    }

    /**
     * Disconnect
     * @return success
     */
    public static SeRequest<Boolean> disconnect() {
        return new SeRequest<>(Boolean.class, RequestType.DISCONNECT);
    }

    /**
     * Request an observation after executing the sent Command
     * TODO(PP): Replace response type with some SE observation
     */
    public static SeRequest<LegacyObservation> command(AgentCommand command) {
        return new SeRequest<>(LegacyObservation.class, RequestType.AGENTCOMMAND, command);
    }

}
