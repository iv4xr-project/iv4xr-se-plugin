package agents;

import agents.tactics.*;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;
import world.LabEntity;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Test that the agent can explore its way to find an entity (a button). 
 * The setup is a simple maze-like room. The room contains another room
 * which is initially unreachable due to a close door guarding it. In
 * the original implementation this subroom causes the explore-tactic to
 * get stuck (because it thinks the subroom is reachable, so it insists on
 * steering the agent to go there; or, when that problem is fixed, the
 * pathfinding algorithm ends up blocking too many navigation triangles, hence
 * making the agent unable to path-plan anywhere; the problem is "fixed",
 * and this test is to test this solution).
 */
public class Explore_2_Test {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
       	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(USE_SERVER_FOR_TEST) labRecruitsTestServer.close(); }

    
    /**
     * Test that the agent can continue to explore despite a nearby closed-room.
     */
    @Test
    public void test_explore_on_maze_with_closedroom() throws InterruptedException {

    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("button1_opens_door1_v2"));
    	
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("agent1")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        
        var g = GoalLib.entityInteracted("button1") ;
        
        agent.setGoal(g) ;

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    	
        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + ", " + agent.getState().id 
            		           + " @" + agent.getState().worldmodel.position
            		           + " V=" + agent.getState().derivedVelocity()) ;
            Thread.sleep(50);
            i++ ;
            if (i>100) {
            	break ;
            }
        }
        
        g.printGoalStructureStatus();
        
        assertTrue(g.getStatus().success()) ;
        var agent_p  = agent.getState().worldmodel.getFloorPosition() ;
        var button_p = ((LabEntity) agent.getState().worldmodel.getElement("button1")).getFloorPosition() ;
        assertTrue(agent_p.distance(button_p) < 0.5) ;

        if (!environment.close())
            throw new InterruptedException("Unity refuses to start the Simulation!");

    }
    
}
