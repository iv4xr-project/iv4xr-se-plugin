package onlineTestCaseGenerator;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import nl.uu.cs.aplib.AplibEDSL;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import uuspaceagent.SEBlockFunctions;
import uuspaceagent.TestUtils;
import uuspaceagent.UUGoalLib;
import uuspaceagent.UUSeAgentState;

import static nl.uu.cs.aplib.AplibEDSL.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

public class Test_ChainPattern {

    public Pair<TestAgent, UUSeAgentState> deployAgent() throws InterruptedException {
        var agentAndState = loadSE("islanddswithdoorsWithDoors") ; // loadSE("myworld-3")  ;
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

    @Test
    public void test_navigate_and_grind() throws InterruptedException {
        // This is a position that is unreachable, so this goal should abort
        console("*** start test...") ;
        var agentAndState = deployAgent();
        TestAgent agent = agentAndState.fst ;
        var state = agentAndState.snd ;
        state.navgrid.enableFlying = true ;
        agent.setTestDataCollector(new TestDataCollector()) ;

      //  GoalStructure G = SEQ(DEPLOYonce(agent, UUGoalLib.closeTo(new Vec3(2.5f,-3.75f,-3f))),
        GoalStructure G = AplibEDSL.SEQ(
                DEPLOYonce(agent, UUGoalLib.closeTo(agent,
                        "BasicAssembler",
                        SEBlockFunctions.BlockSides.FRONT,
                        20f,
                        0.5f)),
                UUGoalLib.targetBlockOK(agent, e ->
                        "BasicAssembler".equals(e.getStringProperty("blockType"))
                        && (float) e.getProperty("integrity") == (float) e.getProperty("maxIntegrity"),
                        false
                ),
              //  UUGoalLib.photo("C:\\workshop\\projects\\iv4xr\\Screenshots\\LargeBlockBatteryBlock.png"),
                UUGoalLib.grinded(agent,0.1f),
                UUGoalLib.targetBlockOK(agent, e ->
                                (float) e.getProperty("integrity") <= 0.1f * (float) e.getProperty("maxIntegrity"),
                        false
                ),
                //Open door
                DEPLOYonce(agent, UUGoalLib.closeTo(agent,
                        "LargeBlockSlideDoor",
                        SEBlockFunctions.BlockSides.BACK,
                        20f,
                        0.1f))
                ,
                UUGoalLib.interacted(agent),
                //look for the second assembler
                DEPLOY(agent, UUGoalLib.closeTo(agent,
                        "BasicAssembler",
                        SEBlockFunctions.BlockSides.FRONT,
                        0.5f)),
                UUGoalLib.targetBlockOK(agent, e ->
                                "BasicAssembler".equals(e.getStringProperty("blockType"))
                                        && (float) e.getProperty("integrity") == (float) e.getProperty("maxIntegrity"),
                        false
                ),
                //  UUGoalLib.photo("C:\\workshop\\projects\\iv4xr\\Screenshots\\LargeBlockBatteryBlock.png"),
                UUGoalLib.grinded(agent,0.1f),
                UUGoalLib.targetBlockOK(agent, e ->
                                (float) e.getProperty("integrity") <= 0.1f * (float) e.getProperty("maxIntegrity"),
                        false
                ),

                //fly to last block
                DEPLOY(agent, UUGoalLib.closeTo(agent,
                        "BasicAssembler",
                        SEBlockFunctions.BlockSides.FRONT,
                        0.5f)),
                UUGoalLib.targetBlockOK(agent, e ->
                                "BasicAssembler".equals(e.getStringProperty("blockType"))
                                        && (float) e.getProperty("integrity") == (float) e.getProperty("maxIntegrity"),
                        false
                ),
                //  UUGoalLib.photo("C:\\workshop\\projects\\iv4xr\\Screenshots\\LargeBlockBatteryBlock.png"),
                UUGoalLib.grinded(agent,0.1f),
                UUGoalLib.targetBlockOK(agent, e ->
                                (float) e.getProperty("integrity") <= 0.1f * (float) e.getProperty("maxIntegrity"),
                        false
                )

        );
        Thread.sleep(5000);
        test_Goal(agent, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
      //  assertTrue(agent.getTestDataCollector().getNumberOfPassVerdictsSeen() == 2) ;
    }
}
