/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/
package testAgents;

import agents.GymAgent;
import agents.tactics.GoalStructureFactory;
import environments.EnvironmentConfig;
import environments.GymEnvironment;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;

import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static testAgents.TestSettings.*;

public class FireHazardAgentIndirect {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
        if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PROJECT_BUILD_PATH);
            labRecruitsTestServer.waitForGameToLoad();
        }
    }

    @AfterAll
    static void close() {
        if(USE_SERVER_FOR_TEST)
            labRecruitsTestServer.close();
    }

    /**
     * This demo will tests if an agent can escape the fire hazard level
     */
    //@Test
    public void fireHazardDemo() throws InterruptedException{
        //Add the level to the resources and change the string in the environmentConfig on the next line from Ramps to the new level
        var gym = (GymEnvironment) new GymEnvironment(new EnvironmentConfig("HZRDIndirect").replaceAgentMovementSpeed(0.2f)).registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        QArrayList<GymAgent> agents = new QArrayList<>(new GymAgent[] {
                createHazardAgent(gym)
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
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    /**
     * This method will create an agent which will move through the fire hazard level
     */
    public static GymAgent createHazardAgent(GymEnvironment gym){
        BeliefState state = new BeliefState().setEnvironment(gym);
        GymAgent agent = new GymAgent(state, "0", "");

        //the goals are in order
        //add move goals with GoalStructureFactory.reachPositions(new Vec3(1,0,1)) it can take either a single or multiple vec3
        //set interaction with the buttons with GoalStructureFactory.reachAndInteract("CB3") using the id of the button
        //You will probably not want to explore to prevent random behaviour. in order to do this set the waypoints close enough to each other

        agent.setGoal(SEQ(
                GoalStructureFactory.reachPositions(new Vec3(6,0,5), new Vec3(8,0,1), new Vec3(13,4,1)),
                GoalStructureFactory.reachAndInteract("b4.1"),
                GoalStructureFactory.reachPositions(new Vec3(13,4,3)),
                GoalStructureFactory.reachAndInteract("b7.1"),
                GoalStructureFactory.reachPositions(new Vec3(9,4,9), new Vec3(8,4,6), new Vec3(5,4,7)),
                GoalStructureFactory.reachAndInteract("b8.2"),
                GoalStructureFactory.reachPositions(new Vec3(1,4,13)),
                GoalStructureFactory.reachAndInteract("b5.1"),
                GoalStructureFactory.reachPositions(new Vec3(1,4,22), new Vec3(6,0,22)),
                GoalStructureFactory.reachAndInteract("b1.1"),
                GoalStructureFactory.reachPositions(new Vec3(5,0,25))
                ));
        return agent;
    }
}
