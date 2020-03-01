/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package testAgents;

import agents.GymAgent;
import agents.tactics.GoalStructureFactory;
import agents.tactics.TacticsFactory;
import environments.EnvironmentConfig;
import environments.GymEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;
import world.Entity;
import world.InteractiveEntity;

import static nl.uu.cs.aplib.AplibEDSL.*;
import static testAgents.TestSettings.*;

public class ButtonCheckerTest {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
            labRecruitsTestServer.waitForGameToLoad();
        }
    }

    @AfterAll
    static void close() {
        if(USE_SERVER_FOR_TEST)
            labRecruitsTestServer.close();
    }

    @Test
    public void buttonWorksTest(){
    	
    	var buttonToTest = "button1" ;
    	var doorToTest = "door1" ;

        // Create an environment
        GymEnvironment environment = new GymEnvironment(new EnvironmentConfig("button1_opens_door1"));
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        try {
	        environment.startSimulation(); // presses "Play" in the game for you
	
	        // create a belief state
	        var state = new BeliefState();
	        state.id = "agent1"; // matches the ID in the CSV file
	        state.setEnvironment(environment); // attach the environment
	
	        // setting up a test-data collector:
			var dataCollector = new TestDataCollector();
	
	        // create a test agent
	        var testAgent = new GymAgent(state) ;
	        
	        // define the test-goal:
	        var goal = SEQ(
	    		MySubGoals.justObserve(),
	            // observe the button to be inactive and the door to be closed
	            GoalStructureFactory.inspect(buttonToTest, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
	            GoalStructureFactory.inspect(doorToTest, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
	            // walk to the button
	            GoalStructureFactory.reachObject(buttonToTest).lift(),
	            // press the button
	            MySubGoals.pressButton(buttonToTest),
	            
	            // now we should check that the button is indeed in its active state, and 
	            // the door is open:
	            GoalStructureFactory.checkObjectInvariant(testAgent,
	            		buttonToTest, 
	            		"button should be active", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
	            GoalStructureFactory.checkObjectInvariant(testAgent,
	            		doorToTest, 
	            		"door should be open", 
	            		(Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive)
	        );
	        
	        testAgent . setTestDataCollector(dataCollector) . setGoal(goal) ;
	
	        //goal not achieved yet
	        assertFalse(testAgent.success());
	
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
        }
        finally { environment.close(); }
    }
}

/**
 * A helper class for constructing support subgoals.
 */
class MySubGoals {
	
	// to just observe the game ... to get information
    static GoalStructure justObserve(){
        return goal("observe").toSolve((BeliefState b) -> b.position != null).withTactic(TacticsFactory.observe()).lift();
    }
    
    static GoalStructure observeInteractiveEntity(String interactiveEntityId, boolean isActive) {
        String goalName = "Observe that " + interactiveEntityId + " is " + (isActive ? "" : "not ") + "active";
        return goal(goalName)
                .toSolve((BeliefState belief) -> {
                    System.out.println(goalName);
                    var interactiveEntities = new QArrayList<>(belief.getAllInteractiveEntities());
                    return interactiveEntities.contains(entity -> entity.id.equals(interactiveEntityId) && entity.isActive == isActive);
                })
                // in the future this will be swapped by inspect(objectId) for when an object is not within sight
                .withTactic(TacticsFactory.observe())
                .lift()
                .maxbudget(1);
    }
	
    // A goal that is reached whenever the buttonId is observed to be pressed (active)
    static GoalStructure pressButton(String buttonId) {
        return
        goal("Press " + buttonId)
            .toSolve((BeliefState belief) -> {
                // the belief should contain an interactive entity (buttonId) that is observed to be pressed (active)
                var interactiveEntities = new QArrayList<>(belief.getAllInteractiveEntities());
                return interactiveEntities.contains(entity -> entity.id.equals(buttonId) && entity.isActive);
            })
            .withTactic(
                FIRSTof(
                    // try to interact
                    TacticsFactory.interact(buttonId),
                    // move toward the button if the agent cannot interact
                    TacticsFactory.move(buttonId)
                )
            ).lift();
    }
    
    // this will be the top level goal
    static GoalStructure test_thisButton_triggers_thatObject(String buttonId, String target) {
        return SEQ(
          justObserve(),
          // observe the button to be inactive and the door to be closed
          GoalStructureFactory.inspect(buttonId, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
          GoalStructureFactory.inspect(target, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
          // walk to the button
          GoalStructureFactory.reachObject(buttonId).lift(),
          // press the button
          pressButton(buttonId),
          // observe the button to be active and the door to be open
          GoalStructureFactory.inspect(buttonId, (Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
          GoalStructureFactory.inspect(target, (Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive)
        );
    }
}