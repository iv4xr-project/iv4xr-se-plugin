/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions ;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_INSTRUMENT;
import static agents.TestSettings.USE_SERVER_FOR_TEST;

import agents.LabRecruitsTestAgent;
import agents.tactics.GoalLib;
import environments.EnvironmentConfig;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import environments.LabRecruitsEnvironment;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.PrintColor;
import nl.uu.cs.aplib.mainConcepts.Environment;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import world.BeliefState;

/**
 * To test simple movement of four agents towards some new positions close by.
 * The setup is a plain square room, with four agents, one in each corner. Each 
 * is given the goal to move to some position somewhere in the center of the
 * room.
 */
public class AgentSimpleMovementTest {
    
	private static LabRecruitsTestServer labRecruitsTestServer = null ;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    
    
    @Test
    public void movementTest() throws InterruptedException {

        var environment = new LabRecruitsEnvironment(new EnvironmentConfig("square"));
        
        var p0 = new Vec3(3, 0, 3) ;
        var p1 = new Vec3(3, 0, 4) ;
        var p2 = new Vec3(4, 0, 3) ;
        var p3 = new Vec3(4, 0, 4) ;
        var ta0 = createAgent("agent0", environment, p0);
        var ta1 = createAgent("agent1", environment, p1) ;
        var ta2 = createAgent("agent2", environment, p2) ;
        var ta3 = createAgent("agent3", environment, p3) ;
        
        QArrayList<LabRecruitsTestAgent> agents = new QArrayList<>(new LabRecruitsTestAgent[] { ta0,ta1,ta2,ta3 });
         
        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int tick = 0;
        while (!agents.allTrue(LabRecruitsTestAgent::success)){
        	
            System.out.println("TICK " + tick );

            // only updates in progress
            for(var agent : agents.where(agent -> !agent.success())){
            	System.out.println("*** " + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
                agent.update();
                if(agent.success()) agent.printStatus();
            }
            Thread.sleep(15);
            tick++;
            if (tick >= 30) {
            	// takes too long, something is wrong..
            	Assertions.fail("The agents run too long...") ;
            	break ;
            }
        }
        System.out.println("*** Distance " + ta0.getState().id + " to dest:" 
                           + ta0.getState().worldmodel.getFloorPosition().distance(p0)) ;
        System.out.println("*** Distance " + ta1.getState().id + " to dest:" 
                           + ta1.getState().worldmodel.getFloorPosition().distance(p1)) ;
        System.out.println("*** Distance " + ta2.getState().id + " to dest:" 
                           + ta2.getState().worldmodel.getFloorPosition().distance(p2)) ;
        System.out.println("*** Distance " + ta3.getState().id + " to dest:" 
                           + ta3.getState().worldmodel.getFloorPosition().distance(p3)) ;
        assertTrue(ta0.getState().worldmodel.getFloorPosition().distance(p0) < 0.5) ;
        assertTrue(ta1.getState().worldmodel.getFloorPosition().distance(p1) < 0.5) ;
        assertTrue(ta2.getState().worldmodel.getFloorPosition().distance(p2) < 0.5) ;
        assertTrue(ta3.getState().worldmodel.getFloorPosition().distance(p3) < 0.5) ;
   
	    if (!environment.close())
	        throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    /**
     * Create a fresh test-agent, with a single goal to move to the given destination.
     */
    private static LabRecruitsTestAgent createAgent(String id, LabRecruitsEnvironment env, Vec3 dest){
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(id)
        		                    . attachState(new BeliefState())
        		                    . attachEnvironment(env) ;
        
        agent.setGoal(GoalLib.positionInCloseRange(dest).lift());
        return agent;
    }
}