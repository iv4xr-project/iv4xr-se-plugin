package agents;

import static agents.TestSettings.USE_SERVER_FOR_TEST;


import game.LabRecruitsTestServer;
import helperclasses.datastructures.Vec3;
import world.BeliefState;
import world.LabWorldModel;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import agents.tactics.GoalLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public class VisibilityTest {
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	TestSettings.USE_SERVER_FOR_TEST = false ;
    	// Uncomment this to make the game's graphic visible:
    	TestSettings.USE_GRAPHICS = true ;
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
    	
    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("visibilitytest"));
    	
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
       
       
       
       // toggle button0 to open the door 
       Thread.sleep(30);
       wom = environment.moveToward("agent0", wom.position, new Vec3(3,0,3)) ; 
       Thread.sleep(30);
       wom = environment.interactWith("agent0","button0") ;
       
       // wait to let the door open completely, then observe
       Thread.sleep(500);
       wom = environment.observe("agent0") ;
       assertNotNull(wom.getElement("button0")) ;
       assertNotNull(wom.getElement("door0")) ;
       assertNull(wom.getElement("button1")) ;
       
       // move to pass the door and observe again:
       Thread.sleep(50);
       wom = environment.moveToward("agent0", wom.position, new Vec3(4,0,3)) ; 
       Thread.sleep(50);
       wom = environment.moveToward("agent0", wom.position, new Vec3(5,0,3)) ; 
       Thread.sleep(30);
       wom = environment.moveToward("agent0", wom.position, new Vec3(6,0,3)) ; 
       Thread.sleep(30);
  
       wom = environment.observe("agent0") ;

       assertNotNull(wom.getElement("button0")) ;
       assertNotNull(wom.getElement("door0")) ;
       assertNotNull(wom.getElement("button1")) ;

       
       // move further into the room:
       wom = environment.moveToward("agent0", wom.position, new Vec3(7,0,3)) ; 
       Thread.sleep(30);
       wom = environment.moveToward("agent0", wom.position, new Vec3(8,0,3)) ; 
       Thread.sleep(30);
       wom = environment.observe("agent0") ;
       
       assertEquals(2,countDecoration(wom,"FireHazard")) ; // can see 2x fires
       assertNotNull(wom.getElement("guard")) ; // can see an NPC named "guard"
       assertNotNull(wom.getElement("FLAG")) ; // can see a goal named "FLAG"
       
       // The following objects are not sent over by LR :|.. 
       //    fire extinguisher, because it is a sub-object?
       //    other agents 
       
       
       if (!environment.close())
           throw new InterruptedException("Unity refuses to start the Simulation!");

    }

}
