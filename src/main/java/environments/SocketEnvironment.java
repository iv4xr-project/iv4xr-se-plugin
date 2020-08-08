/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import logger.PrintColor;
import nl.uu.cs.aplib.utils.Pair;
import pathfinding.Pathfinder;
import eu.iv4xr.framework.exception.Iv4xrError;
import eu.iv4xr.framework.mainConcepts.W3DEnvironment;
import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Mesh;
import world.LabRecruitsRawNavMesh;
import world.LegacyDynamicEntity;
import world.LegacyEntity;
import world.LegacyEntityType;
import world.LegacyInteractiveEntity;
import world.LegacyObservation;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.Socket;

public class SocketEnvironment extends W3DEnvironment {
	
	private LabRecruitsConfig gameconfig ;

	/**
	 * Limiting the agent speed per update cycle to 2 distance unit.
	 */
	public static final float AGENTSPEED = 2f ;

	private SocketReaderWriter socket ;
    
    /**
     * Constructor.
     */
    public SocketEnvironment(LabRecruitsConfig gameConfig) {
    	this.gameconfig = gameConfig ;
    	socket = new SocketReaderWriter(gameConfig.host, gameConfig.port) ;
    	loadWorld() ;
    }
    
    public LabRecruitsConfig gameConfig() { 
    	return gameconfig ;
    }
    
    @Override
    public void loadWorld() {
		var rawmesh = (LabRecruitsRawNavMesh) sendCommand(null,null,LOADWORLD_CMDNAME,null,LabRecruitsRawNavMesh.class) ;
		if (rawmesh==null) 
			throw new Iv4xrError("Fail to load the navgation-graph of the world") ;
		worldNavigableMesh = rawmesh.covertToMesh() ;
	}
    
    /**
     * @param cmd representing the command to send to the real environment.
     * @return an object that the real environment sends back as the result of the
     * command, if any.
     */
    @Override
    protected Object sendCommand_(EnvOperation cmd) {
    	// The way the communication with Lab Recruit works, an EnvOperation cannot be
    	// send directly to LR. Instead, we need to translate it first to the right
    	// instance of "Request" object.
        try {
        	 if (cmd.command.equals(LOADWORLD_CMDNAME)) {
             	return sendPackage(Request.gymEnvironmentInitialisation(gameconfig)) ;
             }
             if (cmd.command.equals(OBSERVE_CMDNAME)) {
             	LegacyObservation obs = (LegacyObservation) sendPackage(Request.command(AgentCommand.doNothing(cmd.invokerId))) ;
             	return LegacyObservation.toWorldModel(obs) ;
             }
             if (cmd.command.equals(MOVETOWARD_CMDNAME)) {
            	 Vec3 agentLocation = ((Pair<Vec3,Vec3>) cmd.arg).fst ;
            	 Vec3 targetLocation = ((Pair<Vec3,Vec3>) cmd.arg).snd ;
            	 
                 //Calculate the move direction:
                 Vec3 direction = Vec3.sub(agentLocation, targetLocation);
                 if (direction.length() > AGENTSPEED) {
                	 // the distance is too far, given the agent speed. Calculate a new
                	 // target position, which is reachable given the speed:
                	 direction = direction.normalized() ;
                	 direction = Vec3.mul(direction, AGENTSPEED) ;
                	 targetLocation = Vec3.add(agentLocation, direction) ;
                 } 
                 boolean jump = false ; // for now, we will not use jumps
                 sendPackage(Request.command(AgentCommand.moveTowardCommand(cmd.invokerId,targetLocation,jump))) ;
             }
             throw new IllegalArgumentException();
        }
        catch (IOException ex) {
           System.out.println("I/O error: " + ex.getMessage());
           return null;
        }
    }
    
    /**
     * Close the socket and connection with the Lab Recruits.
     */
    public boolean close() {
    	try {
    		boolean success = sendPackage(Request.disconnect());
    		if(success){
    			socket.close() ;
    		}
    		else {
                System.out.println(String.format("%s: Unity does not respond to a disconnection request.", PrintColor.FAILURE()));
            }
    		return success ;
    	}
    	catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(String.format("%s: Could not disconnect from the host by closing the socket.", PrintColor.FAILURE()));
            return false;
        }
    }
    
    /**
     * Primitive for sending a command-package to Lab-Recruit, and to return its response. 
     * The command to send should be wrapped as a "Request" object. 
     */
    private <T> T sendPackage(Request<T> packageToSend) throws IOException {
    	socket.write(packageToSend);
    	return socket.read(packageToSend.responseType) ;
    }
}
