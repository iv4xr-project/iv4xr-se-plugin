package agents;

import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer = TestSettings.start_LabRecruitsTestServer(labRecruitesExeRootDir) ;
    }

    @AfterAll
    static void close() { if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); }
    

    @Test
    public void testFire1() throws InterruptedException {
    	
    	var environment = new LabRecruitsEnvironment(new EnvironmentConfig("hollowsquare"));

        LabRecruitsTestAgent agent = new LabRecruitsTestAgent("agent0")
        		                     . attachState(new BeliefState())
        		                     . attachEnvironment(environment) ;

        // press play in Unity
        if (! environment.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        var p1 = new Vec3(1,0,8) ;
        var p2 = new Vec3(8,0,8) ;
        var p3 = new Vec3(8,0,1) ;
        var p4 = new Vec3(1,0,1) ;
        
        // Make the agent reach each positon sequentially.
        var g = SEQ(GoalLib.justObserve().lift(),
        		    GoalLib.positionsVisited(p1),
        		    GoalLib.invariantChecked(agent,"p1 is reached", (BeliefState s) -> s.position.distance(p1) < 0.5), 
        		    GoalLib.positionsVisited(p2),
        		    GoalLib.invariantChecked(agent,"p2 is reached", (BeliefState s) -> s.position.distance(p2) < 0.5), 
        		    GoalLib.positionsVisited(p3),
        		    GoalLib.invariantChecked(agent,"p3 is reached", (BeliefState s) -> s.position.distance(p3) < 0.5), 
        		    GoalLib.positionsVisited(p4),
        		    GoalLib.invariantChecked(agent,"p4 is reached", (BeliefState s) -> s.position.distance(p4) < 0.5) 
        		);
        
        var dataCollector = new TestDataCollector() ;
        agent.setTestDataCollector(dataCollector).setGoal(g) ;

        int i = 0 ;
        while (g.getStatus().inProgress()) {
            agent.update();
            i++ ;
            System.out.println("*** " + i + ", " + agent.getState().id + " @" + agent.getState().position) ;
            Thread.sleep(30);
        }
        g.printGoalStructureStatus();
        assertTrue(dataCollector.getNumberOfPassVerdictsSeen() == 4) ;
        
        if (!environment.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");

    	
    }

}
