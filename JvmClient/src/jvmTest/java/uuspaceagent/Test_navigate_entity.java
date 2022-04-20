package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import spaceEngineers.transport.SocketReaderWriterKt;

import java.util.Collection;

import static nl.uu.cs.aplib.AplibEDSL.goal;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

/**
 * Testing 2D grid-based auto-navigation using the navigate2DToTAC tactic.
 */
public class Test_navigate_entity {

    /**
     * Auto-navigate to near position of the desired entity.
     */
    public Pair<TestAgent,GoalStructure> test_navigate_to_entity(String entityType) throws InterruptedException {
        console("*** start test...");
        var agentAndState = loadSE("myworld-3");
        TestAgent agent = agentAndState.fst;
        UUSeAgentState state = agentAndState.snd;
        Thread.sleep(1000);
        state.updateState(state.agentId);

        WorldEntity entity = getEntityByType(state.wom.elements.values(), entityType);

        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position);
        var sqDestination = state.navgrid.gridProjectedLocation(entity.position);
        var centerOfSqDestination = state.navgrid.getSquareCenterLocation(sqDestination);

        /**
         * Hardcoded temporally, we will need to use deviated square for calculation
         */
        float THRESHOLD_SQUARED_DEVIATED_DISTANCE_TO_SQUARE = 10f;

        GoalStructure G = goal("close to entity: " + entityType)
                .toSolve((Pair<Vec3,Vec3> positionAndOrientation) -> {
                    var pos = positionAndOrientation.fst;
                    return Vec3.sub(centerOfSqDestination,pos).lengthSq() <= THRESHOLD_SQUARED_DEVIATED_DISTANCE_TO_SQUARE;
                })
                .withTactic(UUTacticLib.navigateToEntity(entity))
                .lift();

        agent.setGoal(G);

        int turn= 0;
        while(G.getStatus().inProgress()) {
            //console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            //Thread.sleep(50);
            turn++;
            if (turn >= 1400) break;
        }

        SocketReaderWriterKt.closeIfCloseable(state.env().getController());

        return new Pair<>(agent,G);
    }

    private WorldEntity getEntityByType(Collection<WorldEntity> entities, String blockType){
        WorldEntity entity = null;
        for (WorldEntity we : entities) {
            //console("WorldEntity: " + we.toString());
            if (we.properties.get("blockType") != null) {
                //console("Observed WOM entity: " + we.properties.get("blockType").toString());
                if (we.properties.get("blockType").equals(blockType)) {
                    return we;
                }
            }
            // Blocks from grid
            if(we.elements.size() > 0){
                entity = getEntityByType(we.elements.values(), blockType);
            }
        }

        return entity;
    }

    /**
     * Test if is possible to navigate to the button panel entity, by recalculating the pathfinder to a closer destination.
     */
    @Test
    public void test_nav_to_button_console() throws InterruptedException {
        String blockType = "ButtonPanelLarge";
        var agent_and_goal = test_navigate_to_entity(blockType);
        var G = agent_and_goal.snd;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Test if is possible to navigate to the doors entity, by recalculating the pathfinder to a closer destination.
     */
    @Test
    public void test_nav_to_doors() throws InterruptedException {
        String blockType = "LargeBlockSlideDoor";
        var agent_and_goal = test_navigate_to_entity(blockType);
        var G = agent_and_goal.snd;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

}
