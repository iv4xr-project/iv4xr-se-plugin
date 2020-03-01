/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/
package testAgents;

import agents.GymAgent;
import agents.tactics.GoalStructureFactory;
import agents.tactics.TacticsFactory;
import environments.EnvironmentConfig;
import environments.GymEnvironment;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import eu.iv4xr.framework.mainConcepts.TestGoal;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import logger.PrintColor;
import nl.uu.cs.aplib.Logging;
import nl.uu.cs.aplib.multiAgentSupport.ComNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;

import static eu.iv4xr.framework.Iv4xrEDSL.assertTrue_;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testAgents.TestSettings.*;

/**
 * This class will check if the demo for the color switch level works
 */
public class ColorSwitchLevelTest {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
            labRecruitsTestServer.waitForGameToLoad();
        }
    }

    @AfterAll
    static void close() {
        if(USE_SERVER_FOR_TEST)
            labRecruitsTestServer.close();
    }

    /**
     * This demo will tests the color switch level
     */
    @Test
    public void testColorSwitchDemo() throws InterruptedException{
        ComNode communication = new ComNode();

        var gym = new GymEnvironment(new EnvironmentConfig("CLRSWTCH"));
        if(USE_INSTRUMENT)
            gym.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        QArrayList<GymAgent> agents = new QArrayList<>(new GymAgent[] {
                (GymAgent)createButtonPressAgent(gym).registerTo(communication),
                (GymAgent)createColorScreenCheckAgent(gym).registerTo(communication)
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

        //check that no verdict failed
        //assertEquals(0, agents.get(1).getTestDataCollector().getNumberOfFailVerdictsSeen());

        //check if all verdicts succeeded
        assertEquals(3, agents.get(1).getTestDataCollector().getNumberOfPassVerdictsSeen());

        Logging.getAPLIBlogger().info("DEMO END.");

        if (!gym.close())
            throw new InterruptedException("Unity refuses to close the Simulation!");
    }

    /**
     * This method will define the first agent in the color switch level which will press the buttons and will return to a base position each time
     * @return The agent
     */
    public static GymAgent createButtonPressAgent(GymEnvironment gym){
        BeliefState state = new BeliefState().setEnvironment(gym);
        GymAgent agent = new GymAgent(state, "0", "");

        //set the goals
        agent.setGoal(SEQ(
                GoalStructureFactory.reachAndInteract("CB3"), //move to the red button and interact with it
                GoalStructureFactory.sendPing("0", "1").lift(), //send a ping to the other agent
                GoalStructureFactory.reachAndInteract("CB3"), //move to the red button and interact with it
                GoalStructureFactory.reachAndInteract("CB1"), //move to the blue button and interact with it
                GoalStructureFactory.sendPing("0", "1").lift(), //send a ping to the other agent
                GoalStructureFactory.reachAndInteract("CB1"), //move to the blue button and interact with it
                GoalStructureFactory.reachAndInteract("CB2"), //move to the green button and interact with it
                GoalStructureFactory.sendPing("0", "1").lift(), //send a ping to the other agent
                GoalStructureFactory.reachAndInteract("CB2"))); //move to the green button and interact with it

        return agent;
    }

    /**
     * This method will define the second agent which will check the color screen each time a button is pressed
     * in the color switch level
     * @return The agent
     */
    public static GymAgent createColorScreenCheckAgent(GymEnvironment gym){
        BeliefState state = new BeliefState().setEnvironment(gym);
        GymAgent agent = new GymAgent(state, "1", "");

        //wait for the ping to check the color screen with the red button
        TestGoal t1 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticsFactory.receivePing()
        );

        String info1 = "Check if the color screen is red";
        var g1 = t1.oracle(agent, (BeliefState b) -> assertTrue_("", info1, b.evaluateEntity("CS1", e -> e.property.equals("CS 1 0 0")))
        ).lift();

        //wait for the ping to check the color screen with the blue button
        TestGoal t2 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticsFactory.receivePing()
        );

        String info2 = "Check if the color screen is blue";
        var g2 = t2.oracle(agent, (BeliefState b) -> assertTrue_("", info2, b.evaluateEntity("CS1", e -> e.property.equals("CS 0 0 1")))
        ).lift();

        //wait for the ping to check the color screen with the green button
        TestGoal t3 = new TestGoal("Wait for ping").toSolve((BeliefState b) -> {
            if(b.receivedPing){
                b.receivedPing = false;//reset the ping
                return true;
            }
            return false;
        }).withTactic(
                TacticsFactory.receivePing()
        );

        String info3 = "Check if the color screen is green";
        var g3 = t3.oracle(agent, (BeliefState b) -> assertTrue_("", info3, b.evaluateEntity("CS1", e -> e.property.equals("CS 0 1 0")))).lift();

        //set the goals
        agent.setGoal(SEQ(g1,g2,g3));

        var dataCollector = new TestDataCollector();
        agent.setTestDataCollector(dataCollector);

        return agent;
    }
}
