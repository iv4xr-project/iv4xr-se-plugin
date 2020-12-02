/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import logger.PrintColor;
import nl.uu.cs.aplib.utils.Pair;
import eu.iv4xr.framework.exception.Iv4xrError;
import eu.iv4xr.framework.mainConcepts.W3DEnvironment;
import eu.iv4xr.framework.spatial.Vec3;
import world.LabRecruitsRawNavMesh;
import world.LabWorldModel;
import world.Observation;

import java.io.*;

public class LabRecruitsEnvironment extends W3DEnvironment {

	private LabRecruitsConfig gameconfig ;

	/**
	 * Limiting the agent speed per update cycle to 2 distance unit.
	 */
	public static final float AGENTSPEED = 2f ;

	static public String INTERACT_CMDNAME  = "Interact" ;

	private SocketReaderWriter socket ;


	/**
	 * Create an instance of this environment with the standard Lab Recruits configuration.
	 */
	public LabRecruitsEnvironment() {
		this(new LabRecruitsConfig()) ;
	}
    /**
     * Constructor. Create an instance of this environment with the given Lab
     * Recruits configuration.
     */
    public LabRecruitsEnvironment(LabRecruitsConfig gameConfig) {
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

    @Override
    public LabWorldModel observe(String agentId) {
		return (LabWorldModel) super.observe(agentId) ;
	}

    @Override
    public LabWorldModel moveToward(String agentId, Vec3 agentLocation, Vec3 targetLocation) {
    	return (LabWorldModel) super.moveToward(agentId, agentLocation, targetLocation) ;
    }

    @Override
    public LabWorldModel interact(String agentId, String targetId, String interactionType) {
		return (LabWorldModel) sendCommand(agentId, targetId, INTERACT_CMDNAME, null, null);
	}


	// place holder for debugging ... to be removed later
	public Observation obs ;

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
        	 Request<Observation> request ;
             if (cmd.command.equals(OBSERVE_CMDNAME)) {
            	request =  Request.command(AgentCommand.doNothing(cmd.invokerId)) ;
             }
             else if (cmd.command.equals(MOVETOWARD_CMDNAME)) {
            	 Vec3 agentLocation = ((Pair<Vec3,Vec3>) cmd.arg).fst ;
            	 Vec3 targetLocation = ((Pair<Vec3,Vec3>) cmd.arg).snd ;

                 //Calculate the move direction:
                 Vec3 direction = Vec3.sub(targetLocation,agentLocation);
                 if (direction.length() > AGENTSPEED) {
                	 // the distance is too far, given the agent speed. Calculate a new
                	 // target position, which is reachable given the speed:
                	 direction = direction.normalized() ;
                	 direction = Vec3.mul(direction, AGENTSPEED) ;
                	 targetLocation = Vec3.add(agentLocation, direction) ;
                 }
                 boolean jump = false ; // for now, we will not use jumps
                 request = Request.command(AgentCommand.moveTowardCommand(cmd.invokerId,targetLocation,jump)) ;
             }
             else if (cmd.command.equals(INTERACT_CMDNAME)) {
            	 request = Request.command(AgentCommand.interactCommand(cmd.invokerId, cmd.targetId)) ;
             }
             else throw new IllegalArgumentException();
             Observation obs =  sendPackage(request) ;
			 this.obs = obs ; // copying the observation for debugging, for now...
             return Observation.toWorldModel(obs) ;
        }
        catch (IOException ex) {
           System.out.println("I/O error: " + ex.getMessage());
           return null;
        }
    }

    /**
     * Press the "play-button" in Unity. If left unpressed, no simulation/game-play can start.
     */
    public Boolean startSimulation(){
    	try {
    		return sendPackage(Request.startSimulation());
    	}
    	catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(String.format("%s: Sending a start-simulation command fails.", PrintColor.FAILURE()));
            return false;
        }
    }

    /**
     * Press the "pause-button" in Unity. This will pull the Unity-side paused.
     */
    public Boolean pauseSimulation(){
    	try {
    		return sendPackage(Request.pauseSimulation());
    	}
    	catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(String.format("%s: Sending a pause-simulation command fails.", PrintColor.FAILURE()));
            return false;
        }
    }

    /**
     * this function updates the hazards in Unity, which is specified in EnvironmentConfig.
     * Currently broken :| TODO.
     */
    public Boolean updateHazards(){
    	try {
           return sendPackage(Request.updateEnvironment());
    	}
    	catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(String.format("%s: Sending an update-environment command fails.", PrintColor.FAILURE()));
            return false;
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
     * Invoke close(), and if Unity refuses to close, this method will throw an
     * InterruptedException.
     */
    public void closeAndThrow() throws InterruptedException {
    	if (!this.close())
	        throw new InterruptedException("Unity refuses to close the Simulation!");
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
