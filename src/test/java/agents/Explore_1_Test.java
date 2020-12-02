/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import agents.tactics.GoalLib;
import agents.tactics.TacticLib;
import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import game.Platform;
import eu.iv4xr.framework.spatial.Vec3;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;
import world.LabEntity;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_INSTRUMENT;
import static agents.TestSettings.USE_SERVER_FOR_TEST;
import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Test that agent can explore its way to find an entity (a button) in a simple
 * maze. The maze is made of walls and decorative items such as book cases.
 * These type of items are static and solid; so they will be substracted from
 * the navigation mesh.
 */
public class Explore_1_Test {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
        TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
       	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer != null) labRecruitsTestServer.close(); }

    
    /**
     * Test that the agent can find button1 in a simple maze.
     */
    @Test
    public void test_explore_on_simplemaze() throws InterruptedException {

    	var environment = new LabRecruitsEnvironment(new LabRecruitsConfig("smallmaze"));
        
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("agent1")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        
        var g = GoalLib.entityInteracted("button1") ;
        agent.setGoal(g) ;
        
    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.");
    		//new Scanner(System.in).nextLine();
    	}

        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + "/" 
               + agent.getState().worldmodel.timestamp + ", "
               + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            Thread.sleep(30);
            i++ ;
            if (i>120) {
            	break ;
            }
        }
        
        g.printGoalStructureStatus();
        
        assertTrue(g.getStatus().success()) ;
        var agent_p  = agent.getState().worldmodel.getFloorPosition() ;
        var button_p = ((LabEntity) agent.getState().worldmodel.getElement("button1")).getFloorPosition() ;
        assertTrue(Vec3.dist(agent_p,button_p) < 0.5) ;
        
        if (!environment.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }
    
}