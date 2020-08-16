package agents.tactics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import agents.LabRecruitsTestAgent;
import agents.TestSettings;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import game.LabRecruitsTestServer;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import world.BeliefState;

public class TestGoalsLib {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
        TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
       	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer != null) labRecruitsTestServer.close(); }

	void hit_RETURN() {
		if(TestSettings.USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
	}
    
    
    @Test
    public void test_positionIsInCloseRange() throws InterruptedException {
    	test_positionIsInCloseRange("smallmaze","agent1", new Vec3(9,0,1)) ;
    	test_positionIsInCloseRange("buttons_doors_1","agent1", new Vec3(2.5f,0,3)) ;
       	test_positionIsInCloseRange("buttons_doors_1","agent1", new Vec3(9,0,1)) ;
           	
    }
    
    
	void test_positionIsInCloseRange(String levelName, String agentId, Vec3 targetPosition) throws InterruptedException {
    	
		System.out.println("======= Level: " + levelName + ", target position " + targetPosition) ;
		
        var environment = new LabRecruitsEnvironment(new EnvironmentConfig(levelName));
        
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(agentId)
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        
        var g = GoalLib.positionIsInCloseRange(targetPosition).lift() ;
        runAgent(agent,g,120) ;
        assertTrue(g.getStatus().success()) ;
	}
	
	void runAgent(LabRecruitsTestAgent agent, GoalStructure g, int maxIters) throws InterruptedException {
		agent.setGoal(g) ;
		hit_RETURN() ;

        // press play in Unity
        if (! agent.env().startSimulation()) throw new InterruptedException("Unity refuses to start the Simulation!");

        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + "/" 
               + agent.getState().worldmodel.timestamp + ", "
               + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            Thread.sleep(30);
            i++ ;
            if (i>maxIters) {
            	break ;
            }
        }
        g.printGoalStructureStatus();
	}
	
	@Test
	public void test_entityStateRefreshed() throws InterruptedException {
		var levelName = "smallmaze" ;
		var agentId = "agent1" ;
		var entityId = "button1" ;
		var environment = new LabRecruitsEnvironment(new EnvironmentConfig(levelName));
        
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(agentId)
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        
        var g = GoalLib.entityStateRefreshed(entityId).lift() ;
        runAgent(agent,g,120) ;
        assertTrue(g.getStatus().success()) ;
	}

}
