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
import helperclasses.datastructures.linq.QArrayList;
import logger.JsonLoggerInstrument;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import game.Platform;
import game.LabRecruitsTestServer;
import world.BeliefState;
import world.Entity;
import world.InteractiveEntity;

import static nl.uu.cs.aplib.AplibEDSL.*;
import static testAgents.TestSettings.*;

public class ButtonCheckerTest {

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

    @Test
    public void buttonWorksTest(){

        // Create an environment
        GymEnvironment environment = new GymEnvironment(new EnvironmentConfig("button1_opens_door1"));
        if(USE_INSTRUMENT)
            environment.registerInstrumenter(new JsonLoggerInstrument()).turnOnDebugInstrumentation();

        environment.startSimulation(); // presses "Play" in the game for you

        // create a belief state
        var state = new BeliefState();
        state.id = "agent1"; // matches the ID in the CSV file
        state.setEnvironment(environment); // attach the environment

        // create the agent (goal and tactics are constructed within ButtonFinderAgent)
        ButtonCheckerAgent agent = new ButtonCheckerAgent(state, "button1", "door1");

        //goal not achieved yet
        Assert.assertFalse(agent.success());

        // keep updating the agent
        while (agent.Running()) {
            agent.update();
        }

        // goal status should be success
        Assert.assertTrue(agent.success());

        // close
        agent.printStatus();
        environment.close();
    }
}

/**
 * This agent will succeed if the Entity 'buttonId' is indeed the trigger for the given 'target'
 */
class ButtonCheckerAgent extends GymAgent {

    private GoalStructure goal;

    public ButtonCheckerAgent(BeliefState state, String buttonId, String target) {
        super(state);

        goal = buttonTriggersTarget(buttonId, target);

        this.setGoal(goal);
    }


    public boolean Running() {
        // the agent is still in progress if one of the main goals is still in progress
        return goal.getStatus().inProgress();
    }

    private GoalStructure buttonTriggersTarget(String buttonId, String target) {
        return SEQ(
                justObserve(),
                // observe the button to be inactive and the door to be closed
                GoalStructureFactory.inspect(buttonId, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
                GoalStructureFactory.inspect(target, (Entity e) -> (e instanceof InteractiveEntity) && !((InteractiveEntity) e).isActive),
                // walk to the button
                GoalStructureFactory.reachObject(buttonId).lift(),
                // press the button
                pressButton(buttonId),
                // observe the button to be active and the door to be open
                GoalStructureFactory.inspect(buttonId, (Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive),
                GoalStructureFactory.inspect(target, (Entity e) -> (e instanceof InteractiveEntity) && ((InteractiveEntity) e).isActive)
        );
    }

    private GoalStructure justObserve(){
        return goal("observe").toSolve((BeliefState b) -> b.position != null).withTactic(TacticsFactory.observe()).lift();
    }
    // A goal that is reached whenever the buttonId is observed to be pressed (active)
    private GoalStructure pressButton(String buttonId) {
        return
                goal("Press " + buttonId)
                        .toSolve((BeliefState belief) -> {
                            // the belief should contain an interactive entity (buttonId) that is observed to be pressed (active)
                            var interactiveEntities = new QArrayList<>(belief.getAllInteractiveEntities());
                            return interactiveEntities.contains(entity -> entity.id.equals(buttonId) && entity.isActive);
                        })
                        .withTactic(
                                FIRSTof(
                                        // try to interact
                                        TacticsFactory.interact(buttonId),
                                        // move toward the button if the agent cannot interact
                                        TacticsFactory.move(buttonId)
                                )
                        ).lift();
    }

    private GoalStructure observeInteractiveEntity(String interactiveEntityId, boolean isActive) {
        String goalName = "Observe that " + interactiveEntityId + " is " + (isActive ? "" : "not ") + "active";
        return goal(goalName)
                .toSolve((BeliefState belief) -> {
                    System.out.println(goalName);
                    var interactiveEntities = new QArrayList<>(belief.getAllInteractiveEntities());
                    return interactiveEntities.contains(entity -> entity.id.equals(interactiveEntityId) && entity.isActive == isActive);
                })
                // in the future this will be swapped by inspect(objectId) for when an object is not within sight
                .withTactic(TacticsFactory.observe())
                .lift()
                .maxbudget(1);
    }
}
