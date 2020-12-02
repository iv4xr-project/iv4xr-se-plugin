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
import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;
import environments.SocketReaderWriter;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import game.LabRecruitsTestServer;
import helperclasses.datastructures.linq.QArrayList;
import nl.uu.cs.aplib.mainConcepts.BasicAgent;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import world.BeliefState;
import world.LabWorldModel;

/**
 * In this test we are given a simple small room with a button close by and a door.
 * We want to verify that interacting with the button will indeed open the door.
 */
public class SimpleInteractionTest {

	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// TestSettings.USE_SERVER_FOR_TEST = false ;
    	// Uncomment this to make the game's graphic visible:
    	TestSettings.USE_GRAPHICS = true ;
    	SocketReaderWriter.debug = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() {
    	SocketReaderWriter.debug = false ;
    	if(labRecruitsTestServer!=null) labRecruitsTestServer.close();
    }


    @Test
    public void test1() throws InterruptedException {

        // Create an environment
    	var config = new LabRecruitsConfig("button1_opens_door1") ;
    	//config.light_intensity = -100f ; //this does not seem to work
        var environment = new LabRecruitsEnvironment(config);

        youCanRepositionWindow() ;

        environment.startSimulation(); // this will press the "Play" button in the game for you

        // create a test agent
        var testAgent = new LabRecruitsTestAgent("agent1") // matches the ID in the CSV file
    		    . attachState(new BeliefState())
    		    . attachEnvironment(environment);

        // define the test-goal:
        var goal = SEQ(
        	GoalLib.entityInteracted("button1"),
            GoalLib.entityInvariantChecked(testAgent,
            		"button1",
            		"button1 should be active",
            		(WorldEntity e) -> e.getBooleanProperty("isOn")),

            GoalLib.entityStateRefreshed("door1"),

            GoalLib.entityInvariantChecked(testAgent,
            		"door1",
            		"door1 should be open",
            		(WorldEntity e) -> e.getBooleanProperty("isOpen"))

        );
        // attaching the goal and testdata-collector
        var dataCollector = new TestDataCollector();
        testAgent . setTestDataCollector(dataCollector) . setGoal(goal) ;

        // keep updating the agent
        int i = 0 ;
        while (goal.getStatus().inProgress()) {
        	System.out.println("*** " + i + "/"
                    + testAgent.getState().worldmodel.timestamp + ", "
                    + testAgent.getState().id + " @" + testAgent.getState().worldmodel.position) ;
        	var d1 = testAgent.getState().worldmodel.getElement("door1") ;
        	var b1 = testAgent.getState().worldmodel.getElement("button1") ;
        	if (d1 != null)
        	    System.out.println("*** door1 open: " + d1.getBooleanProperty("isOpen")) ;
        	if (b1 != null)
        	    System.out.println("*** button1 on: " + b1.getBooleanProperty("isOn")) ;
        	testAgent.update();
        	i++ ;
        	if (i>=70) break ;
        }

        goal.printGoalStructureStatus();
        //testAgent.printStatus();

        // check that we have passed both tests above:
        assertTrue(dataCollector.getNumberOfPassVerdictsSeen() == 2) ;
        // goal status should be success
        assertTrue(testAgent.success());

        environment.closeAndThrow();
    }

    @Test
    public void test_illegalInteraction() throws InterruptedException {
    	// Create an environment
    	var config = new LabRecruitsConfig("button1_opens_door1") ;
    	LabRecruitsEnvironment environment = new LabRecruitsEnvironment(config);

        youCanRepositionWindow() ;

        environment.startSimulation(); // this will press the "Play" button in the game for you

        // create a test agent
        var agent = new LabRecruitsTestAgent("agent1") // matches the ID in the CSV file
    		    . attachState(new BeliefState())
    		    . attachEnvironment(environment);

        LabWorldModel wom = environment.observe("agent1") ;

        // interacting with button should not turn it, as the agent is not close enough:
        wom = environment.interact("agent1","button1",null) ;
        Thread.sleep(50);
        wom = environment.observe("agent1") ;
        assertFalse(wom.getElement("button1").getBooleanProperty("isOn")) ;

        // interacting with a non-existing entity should not be problem, the in-game
        // agent will not do anything, and LR will simply return an observation:
        wom = environment.interact("agent1","xxx",null) ;

        // interacting with an existing entity, which is not interactable should not
        // be problem either:
        wom = environment.interact("agent1","door1",null) ;

        // should reach this point:
        assertTrue(true) ;


        environment.closeAndThrow();
    }
}
