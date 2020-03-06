/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import agents.tactics.GoalLib;
import agents.tactics.TacticLib;
import agents.tactics.TestGoalFactory;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_INSTRUMENT;
import static agents.TestSettings.USE_SERVER_FOR_TEST;
import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.* ;
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
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
            labRecruitsTestServer.waitForGameToLoad();
        }
    }

    @AfterAll
    static void close() { if(USE_SERVER_FOR_TEST) labRecruitsTestServer.close(); }


    /**
     * Test that the agent can find button1 in a simple maze.
     */
    @Test
    public void test_explore_on_simplemaze() throws InterruptedException {

    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("smallmaze"));
        // set this to true if we want to see the commands send through the Environment
        // USE_INSTRUMENT = true ;
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        BeliefState state = new BeliefState().setEnvironment(environment);
        state.id = "agent1";
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(state);
        
        var g = GoalLib.entityReached("button1").lift() ;
        agent.setGoal(g) ;

        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + ", " + agent.getState().id + " @" + agent.getState().position) ;
            Thread.sleep(30);
            i++ ;
            if (i>120) {
            	break ;
            }
        }
        assertTrue(g.getStatus().success()) ;
        var agent_p = agent.getState().position ;
        var button_p = agent.getState().getEntity("button1").position ;
        assertTrue(agent_p.distance(button_p) < 0.5) ;

        g.printGoalStructureStatus();
        
        if (!environment.close())
            throw new InterruptedException("Unity refuses to start the Simulation!");
    }
    
}