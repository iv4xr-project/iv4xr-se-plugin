package spaceEngineers;

import communication.system.RequestType;
import spaceEngineers.commands.SeAgentCommand;
import spaceEngineers.commands.SeSessionCommand;

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
     */
    public static SeRequest<SeObservation> command(SeAgentCommand command) {
        return new SeRequest<>(SeObservation.class, RequestType.AGENTCOMMAND, command);
    }

    public static SeRequest<Boolean> session(SeSessionCommand sessionCommand) {
        return new SeRequest<>(Boolean.class, RequestType.SESSION, sessionCommand);
    }

}
