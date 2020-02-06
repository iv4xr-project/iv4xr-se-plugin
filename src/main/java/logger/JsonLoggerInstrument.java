/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import communication.agent.AgentCommand;
import communication.system.Request;
import communication.system.RequestType;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

import java.lang.reflect.Modifier;

/**
 * This Instrumenter is only for debugging purposes
 * @author Maurin
 */
public class JsonLoggerInstrument implements Environment.EnvironmentInstrumenter {

    private Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

    /**
     * Every time an EnvOperation is executed (the json is sent), log the operation and its result.
     */
    @Override
    public void update(Environment environment) {

        Environment.EnvOperation envOperation = environment.getLastOperation();

        // don't log refresh
        if(envOperation.command.equals("refresh"))
            return;

        try{
            Request r = gson.fromJson((String) envOperation.arg, Request.class);

            if(r.cmd == RequestType.AGENTCOMMAND){
                updateAgentCommand(gson.fromJson(r.arg.toString(), AgentCommand.class));
            }
            else{
                updateSystemRequest(r);
            }
        }
        catch (JsonSyntaxException e) {
            System.out.println(e.toString());
        }
    }

    private void updateSystemRequest(Request r) {
        System.out.println(String.format("> Sent %s request to %s.",
                PrintColor.YELLOW(r.cmd.name()),
                PrintColor.BLUE("Unity") ));
    }

    private void updateAgentCommand(AgentCommand cmd){
        System.out.println(String.format("> %s invoked %s on %s with argument: %s",
                PrintColor.CYAN(cmd.agentId),
                PrintColor.YELLOW(cmd.cmd.name()),
                PrintColor.CYAN((cmd.agentId.equals(cmd.targetId)) ? "itself" : cmd.targetId),
                PrintColor.PURPLE(cmd.arg ==  null ? "null" : cmd.arg.toString())));
    }

    private String arg(Object obj){
        if (obj == null)
            return "NULL";

        System.out.println(obj.getClass());

        if(obj instanceof Vec3)
            return ((Vec3) obj).toString();
        if(obj instanceof String)
            return (String) obj;
        return "UNKNOWN";
    }

    private String TreemapToString(Object obj){
        if(obj == null)
            return "null";
        //System.out.println(obj.toString());
        try {
            return gson.fromJson(obj.toString(), Vec3.class).toString();
        } catch (JsonSyntaxException e) {}
        try {
            return gson.fromJson(obj.toString(), String.class).toString();
        } catch (JsonSyntaxException e) {}
        return "null";
    }

    /**
     * Notify whenever we restart the environment
     */
    @Override
    public void reset() {
        System.out.println("\n>>>>>>>>>> RESET THE ENVIRONMENT <<<<<<<<<<\n");
    }
}
