package environments;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spaceEngineers.SeRequest;

import java.lang.reflect.Modifier;

public class SeSocketEnvironment extends SocketEnvironment {

    public static final int DEFAULT_PORT = 9678;

    public SeSocketEnvironment(String host, int port) {
        super(host, port);
    }

    public Object sendCommand(EnvOperation cmd) {
        return sendCommand_(cmd);
    }

    // transient modifiers should be excluded, otherwise they will be send with json
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)  // For SpaceEngineers compatibility.
            .create();

    /**
     * This method provides a higher level wrapper over Environment.sendCommand. It
     * calls Environment.sendCommand which in turn will call SocketEnvironment.sendCommand_
     * It will also cast the json back to type T.
     * @param req
     * @param <T> any response type, make sure Unity actually sends this object back
     * @return response
     */
    public <T> T getSeResponse(SeRequest<T> req) {
        // WP note:
        // the actual id of the agent and the id of its target (if it interacts with
        // something) are put inside the req object ... :|
        String json = (String) sendCommand("APlib", "Se", "request", gson.toJson(req));
        // we do not have to cast to T, since req.responseType is of type Class<T>
        //System.out.println(json);
        return gson.fromJson(json, req.responseType);
    }
}
