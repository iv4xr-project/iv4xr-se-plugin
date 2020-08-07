/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.adapters.EntityAdapter;
import communication.adapters.EntityTypeAdapter;
import communication.system.Request;
import logger.PrintColor;
import pathfinding.Pathfinder;
import eu.iv4xr.framework.exception.Iv4xrError;
import eu.iv4xr.framework.mainConcepts.W3DEnvironment;
import eu.iv4xr.framework.spatial.meshes.Mesh;
import world.LabRecruitsRawNavMesh;
import world.LegacyDynamicEntity;
import world.LegacyEntity;
import world.LegacyEntityType;
import world.LegacyInteractiveEntity;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.Socket;

public class SocketEnvironment extends W3DEnvironment {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    
    private LabRecruitsConfig gameconfig ;

    // transient modifiers should be excluded, otherwise they will be send with json
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .registerTypeAdapter(LegacyEntityType.class, new EntityTypeAdapter())
            .registerTypeHierarchyAdapter(LegacyEntity.class, new EntityAdapter())
            .create();

    private void setupSockets(String host, int port) {

        int maxWaitTime = 20000;

        System.out.println(String.format("Trying to connect with %s on %s:%s (will time-out after %s seconds)", PrintColor.BLUE("Unity"), host, port, maxWaitTime/1000));

        long startTime = System.nanoTime();

        while (!socketReady() && millisElapsed(startTime) < maxWaitTime){

            try {
                socket = new Socket(host, port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) { }
        }

        if(socketReady()){
            System.out.println(String.format("%s: Connected with %s on %s:%s", PrintColor.SUCCESS(), PrintColor.UNITY(), host, port));
        }
        else{
            System.out.println(String.format("%s: Could not establish a connection with %s, please start %s before creating a GymEnvironment.", PrintColor.FAILURE(), PrintColor.UNITY(), PrintColor.UNITY()));
        }
    }
    
    public SocketEnvironment(LabRecruitsConfig gameConfig) {
    	this.gameconfig = gameConfig ;
    	setupSockets(gameConfig.host, gameConfig.port) ;
    	
    	super(config.host, config.port);
        // When this application has connected with the environment, an exchange in information takes place:
        // For now, this application sends nothing, and receives a navmesh of the world.
        LabRecruitsRawNavMesh navmesh = getResponse(Request.gymEnvironmentInitialisation(config));

        this.pathFinder = new Pathfinder(navmesh);
    	
    }
    
    @Override
    public void loadWorld() {
		worldNavigableMesh = (Mesh) sendCommand(null,null,LOADWORLD_CMDNAME,null,Mesh.class) ;
		if (worldNavigableMesh==null) 
			throw new Iv4xrError("Fail to load the navgation-graph of the world") ;
	}
    
    public LabRecruitsConfig gameConfig() { 
    	return gameconfig ;
    }

    /**
     * @return true if the socket and readers are not null
     */
    private boolean socketReady(){
        return socket != null && reader != null && writer != null;
    }

    /**
     * @param startTimeNano the start time in long
     * @return the elapsed time from the start time converted to milliseconds
     */
    private float millisElapsed(long startTimeNano){
        return (System.nanoTime() - startTimeNano) / 1000000f;
    }

    /**
     * Close the socket/reader/writer
     */
    public boolean close() {

        // try to disconnect
        boolean success = getResponse(Request.disconnect());

        if(success){
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
                if (socket != null)
                    socket.close();

                System.out.println(String.format("%s: Disconnected from the host", PrintColor.SUCCESS()));

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(String.format("%s: Could not disconnect from the host by closing the socket.", PrintColor.FAILURE()));
                return false;
            }
        }
        else {
            System.out.println(String.format("%s: Unity does not respond to a disconnection request.", PrintColor.FAILURE()));
        }

        return success;
    }

    /**
     * @param cmd representing the command to send to the real environment.
     * @return an object that the real environment sends back as the result of the
     * command, if any.
     */
    @Override
    protected Object sendCommand_(EnvOperation cmd) {
        // The Environment super class uses sendCommand_ to send the json object
        String json = (String) cmd.arg;
        switch (cmd.command) {
            case "debug":
                return debug(json);
            case "request":
                try {
                    // write to the socket
                    writer.println(json);
                    // read from the socket
                    return reader.readLine();
                } catch (IOException ex) {
                    System.out.println("I/O error: " + ex.getMessage());
                    return null;
                }
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method provides a higher level wrapper over Environment.sendCommand. It
     * calls Environment.sendCommand which in turn will call SocketEnvironment.sendCommand_
     * It will also cast the json back to type T.
     * @param req
     * @param <T> any response type, make sure Unity actually sends this object back
     * @return response
     */
    public <T> T getResponse(Request<T> req) {
    	// WP note:
    	// the actual id of the agent and the id of its target (if it interacts with 
    	// something) are put inside the req object ... :|
        String json = (String) sendCommand("APlib", "Unity", "request", gson.toJson(req));
        // we do not have to cast to T, since req.responseType is of type Class<T>
        //System.out.println(json);
        return gson.fromJson(json, req.responseType);
    }

    private String debug(String json){
        System.out.println("SENDING:" + json);
        return null;
    }
}
