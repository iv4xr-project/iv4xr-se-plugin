/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package game;

import agents.LabRecruitsTestAgent;
import agents.tactics.GoalStructureFactory;
import agents.tactics.TacticsFactory;
import environments.EnvironmentConfig;
import environments.LabRecruitsEnvironment;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;

import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import world.BeliefState;
import world.InteractiveEntity;

import java.util.function.Predicate;

import static agents.TestSettings.*;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static nl.uu.cs.aplib.AplibEDSL.goal;

public class SimpleUnityTest {

    private static LabRecruitsTestServer labRecruitsTestServer;

    @BeforeAll
    static void start() {
        if(USE_SERVER_FOR_TEST){
        	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
            labRecruitsTestServer = new LabRecruitsTestServer(
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

    @Test
    public void observePositionTest() {

        var config = new EnvironmentConfig("observePositionTest")
                .replaceSeed(500)
                .replaceFireSpreadSpeed(2f);

        LabRecruitsEnvironment environment = new LabRecruitsEnvironment(config);
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        // create the agent
        var agent = new LabRecruitsTestAgent(new BeliefState("agent0", environment));

        // The agent wants to know its position
        GoalStructure goal = goal(config.level_name)
                .toSolve((BeliefState belief) -> belief.position != null)
                // the agent should find its position just by observing
                .withTactic(TacticsFactory.observe())
                .lift();

        agent.setGoal(goal);

        //goal not achieved yet
        Assertions.assertTrue(goal.getStatus().inProgress());

        // Toggle play in Unity
        Assertions.assertTrue(environment.startSimulation());

        //update one round
        agent.update();

        //agent should now know where it is
        Assertions.assertFalse(goal.getStatus().inProgress());
        goal.printGoalStructureStatus();

        environment.close();
    }

    @Test
    public void observeSwitchTest() {

        var config = new EnvironmentConfig("observeSwitchTest");

        LabRecruitsEnvironment environment = new LabRecruitsEnvironment(config);
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        // create the agent
        var agent = new LabRecruitsTestAgent(new BeliefState("agent0", environment));

        // Entity list contains an entity with type "Switch" and position (1,0,1)
        Predicate<BeliefState> evaluation = (BeliefState belief) -> new QArrayList<>(belief.getAllInteractiveEntities())
                .contains(entity -> entity.id.equals("button0") && entity.position.equals(new Vec3(1, 0, 1)));

        GoalStructure goal = goal(config.level_name)
                .toSolve(evaluation)
                .withTactic(TacticsFactory.observe()) // the agent should be able to see the button by observing
                .lift();

        agent.setGoal(goal);

        //goal not achieved yet
        Assertions.assertTrue(goal.getStatus().inProgress());

        // Toggle play in Unity
        Assertions.assertTrue(environment.startSimulation());

        //update one round
        agent.update();

        //agent should now know where it is
        Assertions.assertFalse(goal.getStatus().inProgress());

        goal.printGoalStructureStatus();

        environment.close();
    }

    /**
     * The agent should only be able to see 1 of the 5 switches. The other 4 are hidden behind walls..
     */
    @Test
    public void observeVisibleSwitches() {

        var config = new EnvironmentConfig("observeVisibleSwitches");

        LabRecruitsEnvironment environment = new LabRecruitsEnvironment(config);
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        // create the agent
        var agent = new LabRecruitsTestAgent(new BeliefState("agent0", environment));

        // Entity list contains an entity with type "Switch" and position (1,0,1)
        Predicate<BeliefState> evaluation = (BeliefState belief) ->
                belief.getAllInteractiveEntities().size() == 1 &&
                new QArrayList<>(belief.getAllInteractiveEntities())
                        .contains(entity -> entity.id.equals("button1") && entity.position.equals(new Vec3(3, 0, 1)));

        GoalStructure goal = goal(config.level_name)
                .toSolve(evaluation)
                .withTactic(TacticsFactory.observe()) // the agent should be able to see the button by observing
                .lift();

        agent.setGoal(goal);

        //goal not achieved yet
        Assertions.assertTrue(goal.getStatus().inProgress());

        // Toggle play in Unity
        Assertions.assertTrue(environment.startSimulation());

        //update one round
        agent.update();

        //agent should now know where it is
        Assertions.assertFalse(goal.getStatus().inProgress());

        goal.printGoalStructureStatus();

        environment.close();
    }

    @Test
    public void moveToButton(){
        var config = new EnvironmentConfig("moveToButton");

        LabRecruitsEnvironment environment = new LabRecruitsEnvironment(config);
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        // create the agent
        var agent = new LabRecruitsTestAgent(new BeliefState("agent0", environment));

        GoalStructure goal = SEQ(
                GoalStructureFactory.reachPosition(new Vec3(1,0,1)).lift(),
                GoalStructureFactory.inspect("button0", e -> !((InteractiveEntity) e).isActive),
                GoalStructureFactory.reachAndInteract("button0"),
                GoalStructureFactory.inspect("button0", e -> ((InteractiveEntity) e).isActive),
                GoalStructureFactory.reachPosition(new Vec3(1,0,1)).lift()
        );

        agent.setGoal(goal);

        //goal not achieved yet
        Assertions.assertTrue(goal.getStatus().inProgress());

        // Toggle play in Unity
        Assertions.assertTrue(environment.startSimulation());

        //update one round
        while (goal.getStatus().inProgress())
            agent.update();

        //agent should now know where it is
        Assertions.assertFalse(goal.getStatus().inProgress());

        goal.printGoalStructureStatus();

        environment.close();
    }
}
