/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.demo;



import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import agents.tactics.GoalLib;
import agents.tactics.TacticLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.mainConcepts.Environment;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;
import world.Entity;
import world.InteractiveEntity;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.*;

/**
 * A simple test to demonstrate using iv4xr agents to test the Lab Recruits game.
 * The testing task is to verify that the closet in the east is reachable from
 * the player initial position, which it is if the door guarding it can be opened.
 * This in turn requires a series of switches and other doors to be opened.
 * 
 * ISSUE to be solved!!
 *    * When the agent believes that a door is closed, it will refuse to navigate
 *      to it (because it is not reachable according to its nav-map).
 *      We need instead navigate to a position close to it to observe it.
 *    * Agent can get stuck in a bending corner!
 */
public class RoomReachabilityTest {

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
    
    void instrument(Environment env) {
    	env.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();
    }

    /**
     * A test to verify that the east closet is reachable.
     */
    @Test
    public void closetReachableTest() throws InterruptedException {
    	
    	var buttonToTest = "button1" ;
    	var doorToTest = "door1" ;

        // Create an environment
        var environment = new LabRecruitsEnvironment(new EnvironmentConfig("buttons_doors_1"));
        if(USE_INSTRUMENT) instrument(environment) ;

        try {
        	if(TestSettings.USE_GRAPHICS) {
        		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
        		new Scanner(System.in) . nextLine() ;
        	}
        	
	        environment.startSimulation(); // this will press the "Play" button in the game for you
	
	        // create a test agent
	        var testAgent = new LabRecruitsTestAgent("agent1") // matches the ID in the CSV file
        		    . attachState(new BeliefState())
        		    . attachEnvironment(environment);
	        
	        // define the test-goal:
	        var goal = SEQ(
	            GoalLib.justObserve().lift(),
		        GoalLib.entityIsInteracted("button1"),
                GoalLib.entityIsInRange_smarter("door1"),
	        	GoalLib.entityInvariantChecked(testAgent,
	            		"door1", 
	            		"door1 should be open", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
	        	
	        	GoalLib.entityIsInteracted("button3"),
	        	GoalLib.entityIsInRange("door2").lift(),
	        	GoalLib.entityInvariantChecked(testAgent,
	            		"door2", 
	            		"door2 should be open", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
	        	GoalLib.entityIsInteracted("button4"),
	        	//GoalLib.entityIsInRange("button3").lift(),
	        	//GoalLib.entityIsInRange("door1").lift(),
	        	GoalLib.entityIsInRange_smarter("door1"),
	        	GoalLib.entityInvariantChecked(testAgent,
	            		"door1", 
	            		"door1 should be open", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
	        	//GoalLib.entityIsInRange("button1").lift(),
	        	GoalLib.entityIsInRange_smarter("door3"),
	        	GoalLib.entityInvariantChecked(testAgent,
	            		"door3", 
	            		"door3 should be open", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive)
	        );
	        // attaching the goal and testdata-collector
	        var dataCollector = new TestDataCollector();
	        testAgent . setTestDataCollector(dataCollector) . setGoal(goal) ;
	
	        //goal not achieved yet
	        assertFalse(testAgent.success());
	
	        int i = 0 ;
	        // keep updating the agent
	        while (goal.getStatus().inProgress()) {
	        	System.out.println("*** " + i + ", " + testAgent.getState().id + " @" + testAgent.getState().position) ;
	            Thread.sleep(30);
	            i++ ; 
	        	testAgent.update();
	        	if (i>400) {
	        		break ;
	        	}
	        }
	        goal.printGoalStructureStatus();
	
	        // check that we have passed both tests above:
	        assertTrue(dataCollector.getNumberOfPassVerdictsSeen() == 4) ;
	        // goal status should be success
	        assertTrue(testAgent.success());
	
	        // close
	        testAgent.printStatus();
        }
        finally { environment.close(); }
    }
}

