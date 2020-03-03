/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_INSTRUMENT;
import static agents.TestSettings.USE_SERVER_FOR_TEST;
import static org.junit.jupiter.api.Assertions.*;

import agents.LabRecruitsTestAgent;
import agents.tactics.GoalStructureFactory;
import environments.EnvironmentConfig;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import environments.LabRecruitsEnvironment;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.PrintColor;
import world.BeliefState;

/**
 * To test simple movement of four agents towards some new positions close by.
 */
public class MovementTest {
    // This application starts a single agent
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	// TestSettings.USE_GRAPHICS = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
            labRecruitsTestServer.waitForGameToLoad();
        }
    }

    @AfterAll
    static void close() { if(USE_SERVER_FOR_TEST) labRecruitsTestServer.close(); }


    @Test
    public void movementTest() throws InterruptedException {

        var environment = new LabRecruitsEnvironment(new EnvironmentConfig("square"));
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        var ta0 = createAgent("agent0", environment, new Vec3(3, 0, 3));
        var ta1 = createAgent("agent1", environment, new Vec3(3, 0, 4)) ;
        var ta2 = createAgent("agent2", environment, new Vec3(4, 0, 3)) ;
        var ta3 = createAgent("agent3", environment, new Vec3(4, 0, 4)) ;
        
        QArrayList<LabRecruitsTestAgent> agents = new QArrayList<>(new LabRecruitsTestAgent[] { ta0,ta1,ta2,ta3 });
         
        try {
	        // press play in Unity
	        if (! environment.startSimulation())
	            throw new InterruptedException("Unity refuses to start the Simulation!");
	
	        int tick = 0;
	        while (!agents.allTrue(LabRecruitsTestAgent::success)){
	        	
	            System.out.println(PrintColor.GREEN("TICK " + tick + ":"));
	
	            // only updates in progress
	            for(var agent : agents.where(agent -> !agent.success())){
	            	System.out.println("*** " + agent.getState().id + " @" + agent.getState().position) ;
	                agent.update();
	                if(agent.success()) agent.printStatus();
	            }
	            Thread.sleep(15);
	            tick++;
	            if (tick >= 30) {
	            	// takes too long, something is wrong..
	            	Assertions.fail("The agents run too long...") ;
	            	break ;
	            }
	        }
        } 
        finally { 
	        if (!environment.close())
	            throw new InterruptedException("Unity refuses to start the Simulation!");
        }
    }

    /**
     * Create a fresh test-agent, with a single goal to move to the given destination.
     */
    private static LabRecruitsTestAgent createAgent(String id, LabRecruitsEnvironment gym, Vec3 dest){
        BeliefState state = new BeliefState().setEnvironment(gym);
        state.id = id;
        LabRecruitsTestAgent agent = new LabRecruitsTestAgent(state);
        agent.setGoal(GoalStructureFactory.positionsVisited(dest));
        return agent;
    }
}