package agents;

import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import agents.tactics.GoalLib;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import helperclasses.datastructures.Vec3;
import world.BeliefState;

public class FireHazardTest {
	
	private static LabRecruitsTestServer labRecruitsTestServer = null ;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    

    //@Test
    public void testFire1() throws InterruptedException {
    	
    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("firetest"));

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
        var g = GoalLib.positionInCloseRange(new Vec3(5,0,1)).lift()   ;  

        
        agent.setGoal(g) ;
        
        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + ": " + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            if (i==1) {
            	System.out.println("check...") ;
            }
            if (i==200) {
           	   break ;
           }
            Thread.sleep(30);
            i++ ;
        }
        g.printGoalStructureStatus();
        
        if (!environment.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");

    	
    }

}
