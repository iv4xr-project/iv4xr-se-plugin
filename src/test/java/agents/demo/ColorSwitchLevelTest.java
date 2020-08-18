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
import eu.iv4xr.framework.mainConcepts.TestGoal;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.Environment;
import nl.uu.cs.aplib.multiAgentSupport.ComNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;

import static agents.TestSettings.*;
import static eu.iv4xr.framework.Iv4xrEDSL.*;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

/**
 * A demo demonstrating a simple multi-agent test. The setup is a level with two
 * separate rooms (there is no path connecting them). One room has (3) buttons
 * and the other has a screen. Pushing the buttons will set a certain color on
 * the screen. The testing task is to verify that each button will indeed set
 * the correct collor.
 * 
 * Two communicating agents are used, one in each room. One is given the task to
 * operate the buttons, and the other is to check the screen. Note that there is
 * no light of sight between the room, so each agent cannot physically see the
 * state of the in-game entities in the other room. The agent communicate in
 * this case by simply sending a ping. More precisely, the agent in the
 * button-room will send a ping each time it has pressed a button. Upon
 * receiving a ping, the other agent will check the screen.
 */
public class ColorSwitchLevelTest {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    
    void instrument(Environment env) {
    	env.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();
    }

    /**
     * A single test implementing the demo.
     */
    @Test
    public void testColorSwitchDemo() throws InterruptedException{
    	        
        var env = new LabRecruitsEnvironment(new EnvironmentConfig("CLRSWTCH"));
        if(USE_INSTRUMENT) instrument(env) ;

        if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
        
        // creating two test agents, Butty and Screeny:
        ComNode communication = new ComNode();
        var butty   =  new LabRecruitsTestAgent("0","")
                       . attachState(new BeliefState())
                       . attachEnvironment(env)
        		       . registerTo(communication) ;
        var screeny = new LabRecruitsTestAgent("1","")
                       . attachState(new BeliefState())
                       . attachEnvironment(env)
 		               . registerTo(communication) 
 		               . setTestDataCollector(new TestDataCollector()) ;
    	
        
        // defining the task for agent Butty:
        var buttyTask = SEQ(
        		GoalLib.entityInteracted("CB3"), //move to the red button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityInteracted("CB3"), //move to the red button and interact with it
                GoalLib.entityInteracted("CB1"), //move to the blue button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityInteracted("CB1"), //move to the blue button and interact with it
                GoalLib.entityInteracted("CB2"), //move to the green button and interact with it
                GoalLib.pingSent("0", "1").lift(), //send a ping to the other agent
                GoalLib.entityInteracted("CB2")); //move to the green button and interact with it
        // and the testing task for agent Screeny:
        var screenyTask = (SEQ(
        		colorIsVerified(screeny,"red","CS 1 0 0").lift(),
        		colorIsVerified(screeny,"blue","CS 0 0 1").lift(),
        		colorIsVerified(screeny,"green","CS 0 1 0").lift()
         )) ;
        // set these goals:
        butty.setGoal(buttyTask) ;
        screeny.setGoal(screenyTask) ;
        
        // press play in Unity
        if (! env.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int tick = 0;
        // run the agent until it solves its goal:
        while (!(butty.success() && screeny.success())){

            System.out.print("** " + tick + ":");
            if (!butty.success()) {
            	butty.update();
            	System.out.print(" agent Butty @" + butty.getState().worldmodel.position) ;
                if (butty.success()) butty.printStatus() ;
            }
            if (!screeny.success()) {
            	screeny.update();
            	if (screeny.success()) butty.printStatus() ;
            }
            System.out.println("") ;
            
            if (tick > 100) {
            	break ;
            }
            Thread.sleep(30);
            tick++;
        }

        //check that no verdict failed
        assertEquals(0, screeny.getTestDataCollector().getNumberOfFailVerdictsSeen());
        //check if all verdicts succeeded
        assertEquals(3, screeny.getTestDataCollector().getNumberOfPassVerdictsSeen());

        if (!env.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    
    // Wait for the ping to check the color screen with the specified color
	// Red  : CS 1 0 0
    // Blue : CS 0 0 1
    // Green: CS 0 1 0
    static TestGoal colorIsVerified(LabRecruitsTestAgent agent, String colorName, String colorCode) {
    	
        TestGoal g = testgoal("Wait for ping")
        		. toSolve((BeliefState b) -> {
                     if(b.receivedPing){
                        b.receivedPing = false;//reset the ping
                        return true;
                     }
                        return false;
                   })
        		
        		. invariant(agent, (BeliefState b) -> 
        		        assertTrue_("", 
        		        		    "Check if the color screen is " + colorName, 
        		             b.evaluateEntity("SCS1", e -> e.getStringProperty("color").equals(colorCode)))
        		        )
        		. withTactic(TacticLib.receivePing());
        
        return g ;
    }
}
