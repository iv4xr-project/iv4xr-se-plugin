package uuspaceagent;

import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.ContextControllerWrapper;
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.model.ToolbarLocation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.*;
import static uuspaceagent.SEBlockFunctions.findWorldEntity;
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

    void console(String str) {
        console(str);
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
        assertTrue(state.grid2D.allObstacleIDs.size() > 0 ) ;
        console(showWOMElements(state.wom)) ;
        console("=========\n") ;
        console("#obstacles:" + state.grid2D.allObstacleIDs.size()) ;

        for(var o : state.grid2D.allObstacleIDs) {
            WorldEntity we = findWorldEntity(state.wom,o) ;
            console("  Obs: " + o + " (" + we.properties.get("blockType") + ")");
            // check is o appears in the map of known obstacles:
            assertTrue(state.grid2D.knownObstacles.values().stream()
                    .anyMatch(obstacles ->
                            obstacles.stream().anyMatch(obs -> obs.obstacle.equals(o)))) ;
        }
        assertTrue(state.grid2D.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("SurvivalKitLarge"))) ;
        assertTrue(state.grid2D.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("Window1x1FlatInv"))) ;
        assertTrue(state.grid2D.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("LargeBlockSlideDoor"))) ;
        assertTrue(state.grid2D.allObstacleIDs.stream()
                .anyMatch(id -> findWorldEntity(state.wom,id).properties.get("blockType").equals("LargeBlockBatteryBlock")));
    }

    /**
     * Test that the pathfinder is able to find a path to a target that is on a clear and straightlne
     * direction from the agent.
     * We will also check pathSmoothing (removing intermediate nodes in straight-line segments in the path).
     */
    //@Test
    public void test_pathfinder1() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        USeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState();
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        //  orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis
        console(showWOMAgent(state.wom));

        // navigating to (10,-5,65) ... this is just before the buttons-panel
        var sqAgent = state.grid2D.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation = state.grid2D.gridProjectedLocation(new Vec3(10,-5,65)) ;

        console(PrintInfos.showObstacle(state,sqAgent));
        console(PrintInfos.showObstacle(state,sqDesitnation));


        List<Pair<Integer,Integer>> path = state.pathfinder2D.findPath(state.grid2D,sqAgent,sqDesitnation) ;

        assertTrue(path.size() > 0) ;
        path = TacticLib.smoothenPath(path) ;
        assertTrue(path.size() > 0) ;
        int k = 0 ;
        for(var sq : path) {
            console(">> Node " + k + ":" + sq + ", center:" + state.grid2D.getSquareCenterLocation(sq));
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
        var agentAndState = loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        USeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState();

        // agent start location should be around: <10.119276,-5.0025,55.681934>
        //  orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis
        console(showWOMAgent(state.wom));

        // navigating to (10,-5,40) ... this is beyond the closed maze where the agent now is.
        // Should not be reachable:
        var sqAgent = state.grid2D.gridProjectedLocation(state.wom.position) ;
        Vec3 dest1 = new Vec3(10,-5,40) ;
        var sqDesitnation1 = state.grid2D.gridProjectedLocation(dest1) ;
        console("Checking path to " + dest1 + " (should be unreachable)");
        List<Pair<Integer,Integer>> path = state.pathfinder2D.findPath(state.grid2D,sqAgent,sqDesitnation1) ;
        assertTrue(path == null) ;

        // destination (10,-5,70). This is actually visible, but the pathfinding does not
        // see that because of a nearby buttons-panel, whose size is marked to be a large
        // block of 1x1x1, but its actually size is smaller.
        // For now, this means that the position is blocked, until we improver the
        // pathfinder (actually it is Grid2DNav that should be improved).
        Vec3 dest2 = new Vec3(10,-5,70) ;
        var sqDesitnation2 = state.grid2D.gridProjectedLocation(dest2) ;
        console("Checking path to " + dest2 + " (should be unreachable)");
        path = state.pathfinder2D.findPath(state.grid2D,sqAgent,sqDesitnation2) ;
        assertTrue(path == null) ;

        // the following destination is near the far window, near the buttons-pannel. Despite
        // the panel that would appear to the pathfinder as blocking more squares that it
        // actually does, there is anough space to reach the destination below.
        Vec3 dest3 = new Vec3(10,-5,73) ;
        var sqDesitnation3 = state.grid2D.gridProjectedLocation(dest3) ;
        console("Checking path to " + dest3 + " (reachable)");
        path = state.pathfinder2D.findPath(state.grid2D,sqAgent,sqDesitnation3) ;
        assertTrue(path.size() > 0 ) ;

        path = TacticLib.smoothenPath(path) ;

        console(">> path.to " + dest3);
        console(PrintInfos.indent(PrintInfos.show2DPath(state,path),5)) ;

        Vec3 dest4 = new Vec3(19,-5,65) ;
        var sqDesitnation4 = state.grid2D.gridProjectedLocation(dest4) ;
        console("Checking path to " + dest4 + " (reachable)");
        console(PrintInfos.showObstacle(state,sqDesitnation4));
        path = state.pathfinder2D.findPath(state.grid2D,sqAgent,sqDesitnation4) ;
        assertTrue(path.size() > 0 ) ;

        path = TacticLib.smoothenPath(path) ;

        console(">> path.to " + dest4);
        console(PrintInfos.indent(PrintInfos.show2DPath(state,path),5)) ;
    }
}
