/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import agents.tactics.GoalStructureFactory;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.BasicAgent;
import world.BeliefState;

// Creates an agent that walks a preset route.
public class ReachPositionsTest {

    //@Test
    public void reachPositions() throws InterruptedException {
        Vec3[] positions = new Vec3[]{
            new Vec3(20, 0, 15)
        };

        // Make the agent reach each positon sequentially.
        var g = GoalStructureFactory.reachPositions(positions);

        var agent = new BasicAgent().attachState(new BeliefState().setEnvironment(new LabRecruitsEnvironment(new EnvironmentConfig("minimal"))));
        agent.setGoal(g);

        while (g.getStatus().inProgress()) {
            agent.update();
            Thread.sleep(30);
        }
        g.printGoalStructureStatus();
    }
}