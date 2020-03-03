/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import agents.tactics.GoalStructureFactory;
import agents.tactics.TestGoalFactory;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.mainConcepts.*;
import world.BeliefState;

import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static org.junit.jupiter.api.Assertions.* ;

// Basic agent that combines movement with interactions to solve Default.csv.
public class DefaultTest {

    //@Test
    public void defaultAgent() throws InterruptedException {

        var g = GoalStructureFactory.buttonsVisited_thenGoalVisited("Goal", "Button");

        var agent = new BasicAgent().attachState(new BeliefState().setEnvironment(new LabRecruitsEnvironment(new EnvironmentConfig("minimal"))));
        agent.setGoal(g);

        while (g.getStatus().inProgress()) {
            agent.update();
            Thread.sleep(30);
        }

        g.printGoalStructureStatus();
    }

    //@Test
    public void defaultTest()  {
        var game_env = new LabRecruitsEnvironment(new EnvironmentConfig("minimal"));
        var state = new BeliefState().setEnvironment(game_env);
        var agent = new TestAgent().attachState(state);
        state.id = "0";

        var goalPosition = new Vec3(7,0,7);

        var info = "Testing Default.csv";

        // Assert button was not pressed when walking to a position.
        var g = TestGoalFactory.reachPosition(goalPosition)
                .oracle(agent, (BeliefState b) -> assertTrue_("", info,
                        b.getInteractiveEntity("Button 1") != null && !b.getInteractiveEntity("Button 1").isActive))
                .lift();

        var dataCollector = new TestDataCollector();
        agent.setTestDataCollector(dataCollector);

        agent.setGoal(g);

        while (g.getStatus().inProgress()) {
            agent.update();
        }

        assertEquals(0, dataCollector.getNumberOfFailVerdictsSeen());
        assertEquals(1, dataCollector.getNumberOfPassVerdictsSeen());
        Logging.getAPLIBlogger().info("TEST END.");

        g.printGoalStructureStatus();
    }
}