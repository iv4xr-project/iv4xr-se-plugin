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
        
        
       LabWorldModel wom = environment.observe("agent0") ;
       
       Thread.sleep(30);
       
       wom = environment.moveToward("agent0", wom.position, new Vec3(3,0,3)) ; 
       Thread.sleep(30);
       wom = environment.interactWith("agent0","button0") ;
       Thread.sleep(500);
       
       wom = environment.observe("agent0") ;
       Thread.sleep(50);
       
       wom = environment.moveToward("agent0", wom.position, new Vec3(4,0,3)) ; 
       Thread.sleep(50);
       
       wom = environment.moveToward("agent0", wom.position, new Vec3(5,0,3)) ; 
       Thread.sleep(30);

       wom = environment.moveToward("agent0", wom.position, new Vec3(6,0,3)) ; 
       Thread.sleep(30);
       
       wom = environment.observe("agent0") ;

       
       if (!environment.close())
           throw new InterruptedException("Unity refuses to start the Simulation!");

    }

}
