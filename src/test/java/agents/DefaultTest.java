/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import agents.tactics.*;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.Vec3;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

// Basic agent that combines movement with interactions to solve Default.csv.
public class DefaultTest {
	
	private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	// Uncomment this to make the game's graphic visible:
    	//TestSettings.USE_GRAPHICS = true ;
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


   
    //@Test
    public void defaultTest()  {
        var game_env = new LabRecruitsEnvironment(new EnvironmentConfig("minimal"));
        var state = new BeliefState().setEnvironment(game_env);
        var agent = new TestAgent().attachState(state);
        state.id = "0";

        var goalPosition = new Vec3(7,0,7);

        var info = "Testing Default.csv";

        // Assert button was not pressed when walking to a position.
        GoalStructure g = null ;
        /* ******
        var g = TestGoalFactory.reachPosition(goalPosition)
                .oracle(agent, (BeliefState b) -> assertTrue_("", info,
                        b.getInteractiveEntity("Button 1") != null && !b.getInteractiveEntity("Button 1").isActive))
                .lift();
        */
        var dataCollector = new TestDataCollector();
        agent.setTestDataCollector(dataCollector);

        agent.setGoal(g);

        while (g.getStatus().inProgress()) {
            agent.update();
        }

        assertEquals(0, dataCollector.getNumberOfFailVerdictsSeen());
        assertEquals(1, dataCollector.getNumberOfPassVerdictsSeen());
        Logging.getAPLIBlogger().info("TEST END.");

        // *** g.printGoalStructureStatus();
    }
}