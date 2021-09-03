package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import spaceEngineers.transport.SocketReaderWriterKt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.*;
import static uuspaceagent.SEBlockFunctions.findWorldEntity;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

/**
 * Testing the underlying pathfinding calculation (we dpn't actually move the agent.
 * We just test that the pathfinder can correctlly calculate paths).
 */
public class Test_Grid2DNav_pathfinding {

    //@Test
    public void test_0() {
        int x = 10 ;
        assertTrue(x == 10) ;
    }


    /**
     * Basic test to check that blocks that should be recognized as obstacles are indeed
     * recognized.
     */
    //@Test
    public void test_obstacles_membership() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        USeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        // do a single update, and check that we if we have the structures:
        state.updateState();

        assertTrue(state.navgrid.allObstacleIDs.size() > 0 ) ;
        console(showWOMElements(state.wom)) ;
        console("=========\n") ;
        console("#obstacles:" + state.navgrid.allObstacleIDs.size()) ;

        for(var o : state.navgrid.allObstacleIDs) {
            WorldEntity we = findWorldEntity(state.wom,o) ;
            console("  Obs: " + o + " (" + we.properties.get("blockType") + ")");
            // check is o appears in the map of known obstacles:
            assertTrue(state.navgrid.knownObstacles.values().stream()
                    .anyMatch(obstacles ->
                            obstacles.stream().anyMatch(obs -> obs.obstacle.equals(o)))) ;
        }
        assertTrue(state.navgrid.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("SurvivalKitLarge"))) ;
        assertTrue(state.navgrid.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("Window1x1FlatInv"))) ;
        assertTrue(state.navgrid.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("LargeBlockSlideDoor"))) ;
        assertTrue(state.navgrid.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("LargeBlockBatteryBlock")));

        SocketReaderWriterKt.closeIfCloseable(state.env());
    }

    /**
     * Return the agent state and a path to the given destination, null if there is none.
     * If the given state is null, a gameworld will be loaded and a single update is done to
     * produce a state.
     */
    Pair<USeAgentState,List<DPos3>> test_pathfinder(USeAgentState state, Vec3 destination) throws InterruptedException {
        if(state == null) {
            var agentAndState = loadSE("myworld-3")  ;
            TestAgent agent = agentAndState.fst ;
            state = agentAndState.snd ;
            Thread.sleep(1000);
            state.updateState();
            state.updateState();

        }

        // agent start location should be around: <10.119276,-5.0025,55.681934>
        //  orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis
        console(showWOMAgent(state.wom));

        // navigating to (10,-5,40) ... this is beyond the closed maze where the agent now is.
        // Should not be reachable:
        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation = state.navgrid.gridProjectedLocation(destination) ;
        List<DPos3> path = state.pathfinder2D.findPath(state.navgrid,sqAgent,sqDesitnation) ;
        return new Pair<>(state,path) ;
    }

    /**
     * Test that the pathfinder is able to find a path to a target that is on a clear and straightlne
     * direction from the agent.
     * We will also check pathSmoothing (removing intermediate nodes in straight-line segments in the path).
     */
    //@Test
    public void test_pathfinder1() throws InterruptedException {
        console("*** start test...") ;
        // navigating to (10,-5,65) ... this is just before the buttons-panel
        Vec3 destination = new Vec3(10,-5,65) ;
        var agent_and_path = test_pathfinder(null,destination) ;
        var state = agent_and_path.fst ;
        var path = agent_and_path.snd ;

        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation = state.navgrid.gridProjectedLocation(destination) ;

        console(PrintInfos.showObstacle(state,sqAgent));
        console(PrintInfos.showObstacle(state,sqDesitnation));

        assertTrue(path.size() > 0) ;
        path = GoalAndTacticLib.smoothenPath(path) ;
        assertTrue(path.size() > 0) ;
        int k = 0 ;
        for(var sq : path) {
            console(">> Node " + k + ":" + sq + ", center:" + state.navgrid.getSquareCenterLocation(sq));
            k++ ;
        }
        // check that the start of the path is the same square as the agent,
        // and last node in the path is the same square as the destinartion squre:
        assertTrue(path.get(0).equals(sqAgent)) ;
        assertTrue(path.get(path.size()-1).equals(sqDesitnation)) ;
    }



    /**
     * This tests path-finding to several locations, some are reachable and some not.
     * @throws InterruptedException
     */
    @Test
    public void test_pathfinder2() throws InterruptedException {
        console("*** start test...") ;

        // navigating to (10,-5,40) ... this is beyond the closed maze where the agent now is.
        // Should not be reachable:
        Vec3 dest1 = new Vec3(10,-5,35) ;
        console("Checking path to " + dest1 + " (should be unreachable)");
        var agent_and_path = test_pathfinder(null,dest1) ;
        var state = agent_and_path.fst ;
        var path = agent_and_path.snd ;
        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation1 = state.navgrid.gridProjectedLocation(dest1) ;
        path = GoalAndTacticLib.smoothenPath(path) ;
        System.out.println("** Path: " + PrintInfos.showPath(state,path));
        assertTrue(path == null) ;

        // destination (10,-5,70). This is actually visible, but the pathfinding does not
        // see that because of a nearby buttons-panel, whose size is marked to be a large
        // block of 1x1x1, but its actually size is smaller.
        // For now, this means that the position is blocked, until we improver the
        // pathfinder (actually it is Grid2DNav that should be improved).
        Vec3 dest2 = new Vec3(10,-5,70) ;
        console("Checking path to " + dest2 + " (should be unreachable)");
        path = test_pathfinder(state,dest2).snd ;
        var sqDesitnation2 = state.navgrid.gridProjectedLocation(dest2) ;
        assertTrue(path == null) ;

        // the following destination is near the far window, near the buttons-pannel. Despite
        // the panel that would appear to the pathfinder as blocking more squares that it
        // actually does, there is anough space to reach the destination below.
        Vec3 dest3 = new Vec3(10,-5,73) ;
        console("Checking path to " + dest3 + " (reachable)");
        var sqDesitnation3 = state.navgrid.gridProjectedLocation(dest3) ;
        path = test_pathfinder(state,dest3).snd ;
        assertTrue(path.size() > 0 ) ;

        path = GoalAndTacticLib.smoothenPath(path) ;

        console(">> path.to " + dest3);
        console(PrintInfos.indent(PrintInfos.showPath(state,path),5)) ;

        // This is a position in front of a sliding-door. It is reachable from the
        // agent's start position.
        Vec3 dest4 = new Vec3(19,-5,65) ;
        console("Checking path to " + dest4 + " (reachable)");
        var sqDesitnation4 = state.navgrid.gridProjectedLocation(dest4) ;
        console(PrintInfos.showObstacle(state,sqDesitnation4));
        path = test_pathfinder(state,dest4).snd ;
        assertTrue(path.size() > 0 ) ;

        path = GoalAndTacticLib.smoothenPath(path) ;

        console(">> path.to " + dest4);
        console(PrintInfos.indent(PrintInfos.showPath(state,path),5)) ;
        SocketReaderWriterKt.closeIfCloseable(state.env());
    }

    /**
     * Just few simple location left and right of the agent to test turning.
     */
    //@Test
    public void test_pathfinder3() throws InterruptedException {
        console("*** start test...") ;

        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(9,-5,55.68f) ;
        console("Checking path to " + dest + " (should be reachable)");
        var agent_and_path = test_pathfinder(null,dest) ;
        var state = agent_and_path.fst ;
        var path = agent_and_path.snd ;
        var sqAgent = state.navgrid.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation1 = state.navgrid.gridProjectedLocation(dest) ;
        assertTrue(path.size() > 0) ;

        path = GoalAndTacticLib.smoothenPath(path) ;
        console(">> path.to " + dest);
        console(PrintInfos.indent(PrintInfos.showPath(state,path),5)) ;

        System.out.println("=========================") ;
        dest = new Vec3(11.5f,-5,55.68f) ;
        console("Checking path to " + dest + " (should be reachable)");
        path = test_pathfinder(state,dest).snd ;
        assertTrue(path.size() > 0) ;
        path = GoalAndTacticLib.smoothenPath(path) ;
        console(">> path.to " + dest);
        console(PrintInfos.indent(PrintInfos.showPath(state,path),5)) ;

        SocketReaderWriterKt.closeIfCloseable(state.env());
    }
}
