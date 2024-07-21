package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.*;
import static uuspaceagent.UUGoalLib.*;

public class Test_Navigate3D {
    public Pair<TestAgent, UUSeAgentState> deployAgent(String worldname) throws InterruptedException {
        // loadSE for NavGrid, loadSE3D for Octree, loadSE3D2 for VoxelGrid
        var agentAndState = loadSE3D(worldname);
        TestAgent agent = agentAndState.fst;
        var state = agentAndState.snd;
        Thread.sleep(1000);
        state.updateState(state.agentId);
        console(showWOMAgent(state.wom));
        return new Pair<>(agent, state);
    }

    public void test_Goal(TestAgent agent, UUSeAgentState state, GoalStructure G) throws InterruptedException {
        agent.setGoal(G);
        Timer.testStart = Instant.now();
        int turn = 0;
        while (G.getStatus().inProgress()) {
            console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            //Thread.sleep(50);
            turn++;
        }
        Timer.endTest();
        Timer.print();

        if (state instanceof UUSeAgentState3DOctree) {
            ((UUSeAgentState3DOctree) state).exportGrid();
        } else if (state instanceof UUSeAgentState3DVoxelGrid) {
            ((UUSeAgentState3DVoxelGrid) state).exportGrid();
        } else if (state instanceof UUSeAgentState2D) {
            ((UUSeAgentState2D) state).exportGrid();
        }
        TestUtils.closeConnectionToSE(state);
    }

    @Test
    public void test_navigate3DToDoor() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("myworld-3 with open door");
        TestAgent agent = agentAndState.fst;
        if (agentAndState.snd instanceof UUSeAgentState2D)
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true ;

        GoalStructure G = DEPLOYonce(agent, UUGoalLib.close3DTo(agent,
                "LargeBlockSlideDoor",
                SEBlockFunctions.BlockSides.FRONT,
                50f,
                0.5f));
        test_Goal(agentAndState.fst, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_navigate3DToBattery() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("myworld-3 Indoors");
        TestAgent agent = agentAndState.fst;
        if (agentAndState.snd instanceof UUSeAgentState2D)
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true ;

        GoalStructure G = DEPLOYonce(agent, UUGoalLib.smartClose3DTo(
                agent,
                "LargeBlockBatteryBlock",
                SEBlockFunctions.BlockSides.FRONT,
                20f,
                0.5f));
        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_navigate3DToBatteryAlt() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("myworld-3 Indoors");
        TestAgent agent = agentAndState.fst;

        GoalStructure G;
        if (agentAndState.snd instanceof UUSeAgentState2D) {
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true;
            G = SEQ(DEPLOYonce(agent, UUGoalLib.closeTo(new Vec3(16.77483f, -1.7425041f, 72.62046f))),
                    DEPLOYonce(agent, closeToButton(0)),
                    pressButton(0, 0),
                    DEPLOYonce(agent, UUGoalLib.close3DTo(
                            agent,
                            "LargeBlockBatteryBlock",
                            SEBlockFunctions.BlockSides.FRONT,
                            20f,
                            0.5f))
            );
        } else {
            G = DEPLOYonce(agent, UUGoalLib.smartClose3DTo(
                    agent,
                    "TargetDummy",
                    SEBlockFunctions.BlockSides.BACK,
                    20f,
                    0.5f));
        }

        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_open_area() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("Glass box"); // Glass box (maze)
        TestAgent agent = agentAndState.fst;
        GoalStructure G;
        if (agentAndState.snd instanceof UUSeAgentState2D) {
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true;
            G = SEQ(DEPLOYonce(agent, closeToButton(0)),
                    UUGoalLib.pressButton(0, 0),
                    DEPLOYonce(agent, close3DTo(
                            agent,
                            "TargetDummy",
                            (UUSeAgentState state) -> (WorldEntity e)
                                    ->
                                    "TargetDummy".equals(e.getStringProperty("blockType")),
                            SEBlockFunctions.BlockSides.BACK,
                            0.5f))
            );
        } else {
            G = DEPLOYonce(agent, UUGoalLib.smartClose3DTo(
                    agent,
                    "TargetDummy",
                    SEBlockFunctions.BlockSides.BACK,
                    20f,
                    0.5f));
        }
        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_explore_open_area() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("Glass box center"); // Glass box (maze) center
        TestAgent agent = agentAndState.fst;
        if (agentAndState.snd instanceof UUSeAgentState2D)
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true ;

        GoalStructure G = DEPLOYonce(agent, UUGoalLib.explore());

        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_open_area_memory() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("Glass box"); // Glass box (maze)
        TestAgent agent = agentAndState.fst;
        if (agentAndState.snd instanceof UUSeAgentState2D)
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true ;

        // Coordinates are the outermost corners
        GoalStructure G = SEQ(
                DEPLOYonce(agent, UUGoalLib.exploreTo(new Vec3(-12,-48,-23))),
                DEPLOYonce(agent, UUGoalLib.exploreTo(new Vec3(97,76,63)))
                );
        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_labrecruits_level() throws InterruptedException {
        console("*** start test...");
        var agentAndState = deployAgent("CR3_3_3_M");
        TestAgent agent = agentAndState.fst;
        if (agentAndState.snd instanceof UUSeAgentState2D)
            ((UUSeAgentState2D) agentAndState.snd).navgrid.enableFlying = true ;

        // viewing range needs to be 20 or higher to ensure the button ids are correct (this is because they are assigned an id in the order they were observed)
        GoalStructure G = SEQ(
                DEPLOYonce(agent, closeToButton(0)),
                UUGoalLib.pressButton(0, 0),

                DEPLOYonce(agent, closeToButton(1)),
                UUGoalLib.pressButton(1, 2),

                DEPLOYonce(agent, closeToButton(3)),
                UUGoalLib.pressButton(3, 3),

                DEPLOYonce(agent, close3DTo(
                        agent,
                        "TargetDummy",
                        (UUSeAgentState state) -> (WorldEntity e)
                                ->
                                "TargetDummy".equals(e.getStringProperty("blockType"))
                                        && Vec3.sub(e.position, state.wom.position).lengthSq() <= 400,
                        SEBlockFunctions.BlockSides.BACK,
                        0.5f
                ))
        );
        test_Goal(agent, agentAndState.snd, G);
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }
}
