/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package testAgents;

import agents.GymAgent;
import agents.tactics.GoalStructureFactory;
import environments.EnvironmentConfig;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import environments.GymEnvironment;
import helperclasses.datastructures.Vec3;
import logger.PrintColor;
import world.BeliefState;

public class MovementTest {
    // This application starts a single agent

    //@Test
    public void movementTest() throws InterruptedException {

        var gym = (GymEnvironment) new GymEnvironment(new EnvironmentConfig("minimal")).registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        QArrayList<GymAgent> agents = new QArrayList<>(new GymAgent[] {
                //CreateAgent("0", gym, new Vec3(23, 0, 10)),
                //CreateAgent("1", gym, new Vec3(11, 0, 27)),
                //CreateAgent("2", gym, new Vec3(32, 0, 3)),
                CreateAgent("3", gym, new Vec3(15, 0, 3)),
                //CreateAgent("4", gym, new Vec3(15, 0, 24))
        });

        // press play in Unity
        if (! gym.startSimulation())
            throw new InterruptedException("Unity refuses to start the Simulation!");

        int tick = 0;
        // run the agent until it solves its goal:
        while (!agents.allTrue(GymAgent::success)){

            System.out.println(PrintColor.GREEN("TICK " + tick + ":"));

            // only updates in progress
            for(var agent : agents.where(agent -> !agent.success())){
                agent.update();
                if(agent.success()){
                    agent.printStatus();
                }
            }
            Thread.sleep(30);
            tick++;
        }

        if (!gym.close())
            throw new InterruptedException("Unity refuses to start the Simulation!");
    }

    private static GymAgent CreateAgent(String id, GymEnvironment gym, Vec3 dest){
        BeliefState state = new BeliefState().setEnvironment(gym);
        state.id = id;
        GymAgent agent = new GymAgent(state);
        agent.setGoal(GoalStructureFactory.reachPositions(dest));
        return agent;
    }
}