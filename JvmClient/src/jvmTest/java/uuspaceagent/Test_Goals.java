package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;

import static nl.uu.cs.aplib.AplibEDSL.DEPLOYonce;
import static nl.uu.cs.aplib.AplibEDSL.SEQ;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static spaceEngineers.transport.SocketReaderWriterKt.closeIfCloseable;
import static uuspaceagent.PrintInfos.*;
import static uuspaceagent.TestUtils.*;

public class Test_Goals {

    public Pair<TestAgent, UUSeAgentState> deployAgent() throws InterruptedException {
        var agentAndState = loadSE("myworld-3 with open door") ; // loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        UUSeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState(state.agentId);
        // agent start location should be around:<10.22475,-5.0025,53.75382>,
        //  orientationForward: <-0.08024501,7.549446E-5,0.99677515> ... so looking towards z-axis
        console(showWOMAgent(state.wom));
        return new Pair<TestAgent, UUSeAgentState>(agent,state) ;
    }

    public void test_Goal(TestAgent agent, UUSeAgentState state, GoalStructure G) throws InterruptedException {
        agent.setGoal(G) ;
        int turn= 0 ;
        while(G.getStatus().inProgress()) {
            //console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            //Thread.sleep(50);
            turn++ ;
            if (turn >= 1400) break ;
        }
        //closeIfCloseable(state.env().getController());
        TestUtils.closeConnectionToSE(state);
    }

    //@Test
    public void test_close2Dto_GS1() throws InterruptedException {
        // This is a position in front of a sliding-door. It is reachable from the
        // agent's start position.
        console("*** start test...") ;
        Vec3 dest = new Vec3(19,-5,65) ;
        var agentAndState = deployAgent();
        var agent = agentAndState.fst ;
        GoalStructure G = DEPLOYonce(agent, UUGoalLib.closeTo(dest)) ;
        test_Goal(agentAndState.fst, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    //@Test
    public void test_close2Dto_GS2() throws InterruptedException {
        // This is a position that is unreachable, so this goal should abort
        console("*** start test...") ;
        Vec3 dest = new Vec3(10,-5,40) ;
        var agentAndState = deployAgent();
        var agent = agentAndState.fst ;
        GoalStructure G = DEPLOYonce(agent, UUGoalLib.closeTo(dest)) ;
        test_Goal(agentAndState.fst, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().failed());
    }

    //@Test
    public void test_closeTo_Block_1() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = deployAgent();
        var agent = agentAndState.fst ;
        GoalStructure G = DEPLOYonce(agent, UUGoalLib.closeTo(agentAndState.fst,
                "LargeBlockSlideDoor",
                SEBlockFunctions.BlockSides.FRONT,
                20f,
                0.5f));
        test_Goal(agentAndState.fst, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    //@Test
    public void test_closeTo_Block_2() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = deployAgent();
        var agent = agentAndState.fst ;
        GoalStructure G = DEPLOYonce(agent, UUGoalLib.closeTo(agentAndState.fst,
                "LargeBlockBatteryBlock",
                SEBlockFunctions.BlockSides.FRONT,
                20f,
                0.5f));
        test_Goal(agentAndState.fst, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    @Test
    public void test_navigate_and_grind() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = deployAgent();
        TestAgent agent = agentAndState.fst ;
        agent.setTestDataCollector(new TestDataCollector()) ;

        GoalStructure G = SEQ(DEPLOYonce(agent, UUGoalLib.closeTo(agent,
                    "LargeBlockBatteryBlock",
                    SEBlockFunctions.BlockSides.FRONT,
                    20f,
                    0.5f)),
                UUGoalLib.targetBlockOK(agent, e ->
                        "LargeBlockBatteryBlock".equals(e.getStringProperty("blockType"))
                        && (float) e.getProperty("integrity") == (float) e.getProperty("maxIntegrity"),
                        false
                ),
                UUGoalLib.photo("C:\\workshop\\projects\\iv4xr\\Screenshots\\LargeBlockBatteryBlock.png"),
                UUGoalLib.grinded(agent,0.5f),
                UUGoalLib.targetBlockOK(agent, e ->
                                (float) e.getProperty("integrity") <= 0.5f * (float) e.getProperty("maxIntegrity"),
                        false
                ),
                UUGoalLib.photo("C:\\workshop\\projects\\iv4xr\\Screenshots\\LargeBlockBatteryBlock_at_50.png")
        );
        Thread.sleep(5000);
        test_Goal(agent, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
        assertTrue(agent.getTestDataCollector().getNumberOfPassVerdictsSeen() == 2) ;
    }
}
