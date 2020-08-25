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
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import world.BeliefState;
import static nl.uu.cs.aplib.AplibEDSL.* ;

public class Test_SamiraLevels {

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
	
	/**
	 * A generic method to create a test-agent, and connect it to the LR game, and 
	 * load the specified level.
	 */
	LabRecruitsTestAgent create_and_deploy_testagent(String levelName, String agentId, String testDescription) {
        System.out.println("======= Level: " + levelName + ", " + testDescription) ;
		
        var environment = new LabRecruitsEnvironment(new EnvironmentConfig(levelName));
        
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(agentId)
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;
        return agent ;
	}
    
	/**
	 * A convenience method to assign a goal to a test agent, and run it on the LR instance
	 * it is connected to.
	 * At the end, the method checks if the goal is indeed achieved.
	 */
	void setgoal_and_run_agent(
			LabRecruitsTestAgent agent, 
			GoalStructure g, 
			int terminationThreshold) throws InterruptedException {
    	
        // give the goal to the agent:
        agent.setGoal(g) ;
		hit_RETURN() ;

        // press play in Unity
        if (! agent.env().startSimulation()) throw new InterruptedException("Unity refuses to start the Simulation!");

        // now run the agent:
        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            System.out.println("*** " + i + "/" 
               + agent.getState().worldmodel.timestamp + ", "
               + agent.getState().id + " @" + agent.getState().worldmodel.position) ;
            Thread.sleep(30);
            i++ ;
            if (i>terminationThreshold) {
            	break ;
            }
        }
        g.printGoalStructureStatus();
        // check that the given goal is solved:
        //assertTrue(g.getStatus().success()) ;
        hit_RETURN() ;

	}
	
	@Test
	public void test1() throws InterruptedException {
		var button1 = "button1" ;
		GoalStructure g = SEQ(
				GoalLib.entityInteracted("button2"),
				GoalLib.entityStateRefreshed("door1"),GoalLib.entityInCloseRange("door1"),
				GoalLib.entityInteracted("button4"),
				GoalLib.entityStateRefreshed("door1"), GoalLib.entityInCloseRange("door1"),
				GoalLib.entityInteracted("button5"),
				GoalLib.entityStateRefreshed("door1"), GoalLib.entityInCloseRange("door1"),
				GoalLib.entityInteracted("button3"),
				GoalLib.entityStateRefreshed("door1"), GoalLib.entityInCloseRange("door1"),
				GoalLib.entityInteracted("button1"),
				GoalLib.entityStateRefreshed("door1"),
				GoalLib.entityInspected("door1", e -> e.getBooleanProperty("isOpen")),
				GoalLib.entityInteracted("button7")
				)
							
				
				;
		var desc = "blabla" ;
		var agent = create_and_deploy_testagent("samiratest_2","agent1",desc) ;
		setgoal_and_run_agent(agent,g,200) ;
	}
}
