package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.SpaceEngineers;
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder;
import spaceEngineers.transport.SocketReaderWriterKt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.PrintInfos.showWOMElements;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

public class Test_pathfinding_entity {

    /**
     * Test that we can connect with the running level and obtain the agent position.
     * I'm really using this test method to extract the Vec3 destination to prepare more
     * pathfinder checks.
     */
    //@Test
    public void test_agent_pos() throws InterruptedException {
        // This is not loading a level, but connecting to an existing one
        SpaceEngineers se = new SpaceEngineersJavaProxyBuilder().localhost("you");
        spaceEngineers.model.Vec3F agentPos = se.getObserver().observe().getPosition();

        assertTrue(agentPos != null);
        console("agent position: " + agentPos);

        SocketReaderWriterKt.closeIfCloseable(se);
    }

    /**
     * Test that if we use the entity position as the destination and we have a non reachable path (because entity bounds),
     * we can recalculate the destination to a close 3D space point to be close to the entity.
     */
    @Test
    public void test_pathfinder_to_entity() throws InterruptedException {
        console("*** start test...");

        var agentAndState = loadSE("myworld-3");

        TestAgent agent = agentAndState.fst;
        UUSeAgentState state = agentAndState.snd;
        Thread.sleep(1000);
        state.navgrid.enableFlying = false ;
        state.updateState(state.agentId);

        //console(showWOMAgent(state.wom));
        //console(showWOMElements(state.wom));
        //console("---------------------------------------");

        // entity pos <8.75,-3.75,70.0> does not work by default
        Vec3 destination = getDestinationFromEntity(state.wom.elements.values(), "ButtonPanelLarge");

        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position);
        var sqDestination = state.navgrid.gridProjectedLocation(destination);
        List<DPos3> path = state.pathfinder2D.findPath(state.navgrid, sqAgent, sqDestination);

        if (path == null) {
            // the pathfinder cannot find a path
            System.out.println("### NO path to " + destination);

            /**
             * MODIFIED:
             * The idea is to check the 3D adjacent positions to find a reachable square,
             * because the original destination aims to the center of an entity.
             */
            // 3D adjacent positions
            //[{-3,-3,-3}{-3,-3,-2}..{0,-1,0}{0,0,-1}{0,0,0},{0,0,1},{0,1,0}..{3,3,2}{3,3,3}]
            List<Vec3> recalculate_adjacent = new ArrayList();
            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    for(int k = 0; k < 4; k++) {
                        recalculate_adjacent.add(new Vec3(i, j, k));
                        recalculate_adjacent.add(new Vec3(i*-1, j*-1, k*-1));
                    }
                }
            }
            System.out.println("Try to recalculate destination with adjacent list: " + Arrays.toString(recalculate_adjacent.toArray()));

            /**
             * MODIFIED:
             * Iterate over all adjacent 3D destinations trying to recalculate the path
             */
            for(Vec3 adjacentDestination : recalculate_adjacent) {
                Vec3 recalculatedDestination = Vec3.add(destination, adjacentDestination);

                sqAgent = state.navgrid.gridProjectedLocation(state.wom.position);
                sqDestination = state.navgrid.gridProjectedLocation(recalculatedDestination);
                path = state.pathfinder2D.findPath(state.navgrid, sqAgent, sqDestination);

                if(path != null) {
                    System.out.println("!!! Path recalculated to " + recalculatedDestination);
                    break;
                } else {
                    System.out.println("### NO modified path to " + recalculatedDestination);
                }
            }
        }

        console(PrintInfos.showObstacle(state,sqAgent));
        console(PrintInfos.showObstacle(state,sqDestination));

        assertTrue(path.size() > 0);
        path = UUTacticLib.smoothenPath(path);
        assertTrue(path.size() > 0);
        int k = 0;
        for(var sq : path) {
            console(">> Node " + k + ":" + sq + ", center:" + state.navgrid.getSquareCenterLocation(sq));
            k++;
        }
        // check that the start of the path is the same square as the agent,
        // and last node in the path is the same square as the destination square:
        assertTrue(path.get(0).equals(sqAgent));
        assertTrue(path.get(path.size()-1).equals(sqDestination));

        SocketReaderWriterKt.closeIfCloseable(state.env().getController());
    }

    private Vec3 getDestinationFromEntity(Collection<WorldEntity> entities, String blockType){
        Vec3 destination = null;
        for (WorldEntity we : entities) {
            //console("WorldEntity: " + we.toString());
            if (we.properties.get("blockType") != null) {
                //console("Observed WOM entity: " + we.properties.get("blockType").toString());
                if (we.properties.get("blockType").equals(blockType)) {
                    return we.position;
                }
            }
            // Blocks from grid
            if(we.elements.size() > 0){
                destination = getDestinationFromEntity(we.elements.values(), blockType);
            }
        }

        return destination;
    }

    /**
     * AStar pathfinder class seems to freeze in a internal loop sometimes.
     * Test that if we try to calculate the path to a non reachable position,
     * we can use a thread to interrupt the pathfinder calculation.
     */
    //@Test
    public void test_pathfinder_loop() throws InterruptedException {
        console("*** start test...");

        var agentAndState = loadSE("LoneSurvivorPlatform");

        TestAgent agent = agentAndState.fst;
        UUSeAgentState state = agentAndState.snd;
        Thread.sleep(1000);
        state.navgrid.enableFlying = false ;
        state.updateState(state.agentId);

        // Custom destination in the platform (it is reachable but the pathfinder calculation fails)
        Vec3 destination = new Vec3(-8.7f,938.2f,15.33f);

        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position);
        var sqDestination = state.navgrid.gridProjectedLocation(destination);

        List<DPos3> path = state.pathfinder2D.findPath(state.navgrid, sqAgent, sqDestination);

        // Path is null because the pathfinder did not work
        assertTrue(path == null);
        console("CustomAStart finished without loop issue");

        SocketReaderWriterKt.closeIfCloseable(state.env().getController());
    }
}
