/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;


import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static agents.TestSettings.*;
import agents.tactics.GoalLib;
import agents.tactics.TacticLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import helperclasses.datastructures.linq.QArrayList;
import nl.uu.cs.aplib.mainConcepts.BasicAgent;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import world.BeliefState;
import world.Entity;
import world.InteractiveEntity;

/**
 * In this test we are given a simple small room with a button close by and a door.
 * We want to verify that interacting with the button will indeed open the door.
 */
public class SimpleInteractionTest {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
   

    @Test
    public void interactionAgent() throws InterruptedException {

        // Create an environment
        var environment = new LabRecruitsEnvironment(new EnvironmentConfig("button1_opens_door1"));
    	
        environment.startSimulation(); // this will press the "Play" button in the game for you

        // create a test agent
        var testAgent = new LabRecruitsTestAgent("agent1") // matches the ID in the CSV file
    		    . attachState(new BeliefState())
    		    . attachEnvironment(environment);
        
        // define the test-goal:
        var goal = SEQ(
            GoalLib.entityReachedAndInteracted("button1"),
            GoalLib.entityInvariantChecked(testAgent,
            		"button1", 
            		"button1 should be active", 
            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),

            GoalLib.entityReached("door1").lift(),
            GoalLib.entityInvariantChecked(testAgent,
            		"door1", 
            		"door1 should be open", 
            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive)
            
        );
        // attaching the goal and testdata-collector
        var dataCollector = new TestDataCollector();
        testAgent . setTestDataCollector(dataCollector) . setGoal(goal) ;

        // keep updating the agent
        while (goal.getStatus().inProgress()) {
        	testAgent.update();
        }

        // check that we have passed both tests above:
        assertTrue(dataCollector.getNumberOfPassVerdictsSeen() == 2) ;
        // goal status should be success
        assertTrue(testAgent.success());

        // close
        testAgent.printStatus();
        if (!environment.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }
}
