package agents;

import static agents.TestSettings.USE_SERVER_FOR_TEST;


import game.LabRecruitsTestServer;
import world.BeliefState;
import world.LabWorldModel;

import static nl.uu.cs.aplib.AplibEDSL.*;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import agents.tactics.GoalLib;
import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public class VisibilityTest {
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
        //TestSettings.USE_SERVER_FOR_TEST = false ;
    	// Uncomment this to make the game's graphic visible:
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
       	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(USE_SERVER_FOR_TEST) labRecruitsTestServer.close(); }
    
    int countDecoration(LabWorldModel wom, String prefix) {
    	int count = 0 ;
    	for(var elemId : wom.elements.keySet()) {
    		if(elemId.startsWith(prefix)) count++ ;
    	}
    	return count ;
    }
    
    
    @Test
    public void visibility_test_1() throws InterruptedException {
    	
    	var environment = new LabRecruitsEnvironment(new LabRecruitsConfig("visibilitytest"));
    	
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("agent0")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        

    	if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    	
    	// press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");
        
       
       // Initial observation:
       LabWorldModel wom = environment.observe("agent0") ;
       // the wom should contain what the agent at that moment sees. Check what it sees
       // and should not see
       assertNotNull(wom.getElement("button0")) ;
       assertNotNull(wom.getElement("door0")) ;
       assertNull(wom.getElement("button1")) ;
       assertEquals(1,countDecoration(wom,"Bookcase")) ;
       assertEquals(1,countDecoration(wom,"Chair")) ;
       assertEquals(2,countDecoration(wom,"Table")) ;
       assertEquals(1,countDecoration(wom,"Desk")) ;
       
       
       var goal = SEQ(GoalLib.entityInteracted("button0"),
    		          GoalLib.entityInteracted("button1"));
       agent . setGoal(goal) ;
       
       int i = 0 ;
       // keep updating the agent
       while (goal.getStatus().inProgress()) {
       	System.out.println("*** " + i + ", " + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
           Thread.sleep(50);
           i++ ; 
       	agent.update();
       	if (i>100) {
       		break ;
       	}
       }
       
       goal.printGoalStructureStatus();
       
       // check what the agent has seen so far. In particular it should now see items
       // in the next room
       assertTrue(goal.getStatus().success()) ;
       
       wom = agent.getState().worldmodel ; 
       assertNotNull(wom.getElement("button0")) ;
       assertNotNull(wom.getElement("door0")) ;
       // checking if items in the next room are by now seen:
       assertNotNull(wom.getElement("button1")) ;
       assertEquals(2,countDecoration(wom,"FireHazard")) ; // can see 2x fires
       assertNotNull(wom.getElement("guard")) ; // can see an NPC named "guard"
       assertNotNull(wom.getElement("FLAG")) ; // can see a goal named "FLAG"
       
       // can just as well check the score and mood :)
       assertEquals(22,wom.score) ;
       //assertTrue(wom.mood.equals("Hmm...")) ;
       
       if (!environment.close())
           throw new InterruptedException("Unity refuses to start the Simulation!");

    }

}
