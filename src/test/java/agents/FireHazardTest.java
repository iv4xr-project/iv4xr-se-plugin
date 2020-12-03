package agents;

import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import agents.tactics.GoalLib;
import environments.LabRecruitsConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import eu.iv4xr.framework.spatial.Vec3;
import world.BeliefState;
import world.LabWorldModel;

public class FireHazardTest {
	
	private static LabRecruitsTestServer labRecruitsTestServer = null ;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_SERVER_FOR_TEST = false ;
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    
    int countDecoration(LabWorldModel wom, String prefix) {
    	int count = 0 ;
    	for(var elemId : wom.elements.keySet()) {
    		if(elemId.startsWith(prefix)) count++ ;
    	}
    	return count ;
    }

    @Test
    // Test that the agent can see fires and a button behind these fires. Then, we ask the
    // agent to interact with the button. Check that this is successful. No avoidance
    // is used, so the agent should just walk through the fire.
    // Check that the hp should then decrease.
    public void testFire1() throws InterruptedException {
    	
    	var environment = new LabRecruitsEnvironment(new LabRecruitsConfig("firetest"));

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

        // Make the agent reach each positon sequentially.
        //var g = GoalLib.positionInCloseRange(new Vec3(5,0,1)).lift()   ;  
        var g = GoalLib.entityInteracted("button0") ; 
        
        agent.setGoal(g) ;
        
        int initialHp = 0 ;
        int i = 0 ;
        LabWorldModel wom = null ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + ": " + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            wom = agent.getState().worldmodel ;
            if (i==0) {
            	// check first observation:
            	System.out.println("** Checking initial observation...") ;
            	assertEquals(6,countDecoration(wom,"FireHazard")) ;
            	assertNotNull(agent.getState().worldmodel.getElement("button0")) ;
            	initialHp = wom.health ;
            	assertEquals(100,initialHp) ;
                assertFalse(agent.getState().isOn("button0")) ;

            }
            if (i==200) {
           	   break ;
           }
            Thread.sleep(30);
            i++ ;
        }
        g.printGoalStructureStatus();
        assertTrue(wom.health < initialHp) ;
        assertTrue(agent.getState().isOn("button0")) ;
        assertTrue(g.getStatus().success()) ;
        
        if (!environment.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");

    	
    }

}
