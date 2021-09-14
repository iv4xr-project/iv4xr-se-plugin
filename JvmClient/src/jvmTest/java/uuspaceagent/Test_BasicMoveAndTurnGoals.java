package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import org.junit.jupiter.api.Test;
import spaceEngineers.transport.SocketReaderWriterKt;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static spaceEngineers.transport.SocketReaderWriterKt.closeIfCloseable;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

/**
 * This tests goals to do basic straight-line 2D move and turning.
 */
public class Test_BasicMoveAndTurnGoals {

    public UUSeAgentState test_Goal(GoalStructure G) throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = loadSE("myworld-3") ;
        TestAgent agent = agentAndState.fst ;
        UUSeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState();

        // agent start location should be around: <10.119276,-5.0025,55.681934>
        //  orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis
        console(showWOMAgent(state.wom));

        agent.setGoal(G) ;

        int turn= 0 ;
        while(G.getStatus().inProgress()) {
            console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            //Thread.sleep(50);
            turn++ ;
            if (turn >= 1400) break ;
        }
        closeIfCloseable(state.env());
        return state ;
    }

    /**
     * Test a simple goal to turn the agent 90 degree to the right.
     */
    //@Test
    public void test_turningRight() throws InterruptedException {
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(9,-5,55.68f) ;
        var G = UUGoalLib.face2DToward(null,dest) ;
        test_Goal(G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Test a simple goal to turn the agent 90 degree to the left.
     */
    //@Test
    public void test_turningLeft() throws InterruptedException {
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(11.5f,-5,55.68f) ;
        var G = UUGoalLib.face2DToward(null,dest) ;
        test_Goal(G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Test strafing to the right.
     */
    //@Test
    public void test_StrafeRight() throws InterruptedException {
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(8,-5,55.68f) ;
        var G = UUGoalLib.veryclose2DTo(null,dest) ;
        test_Goal(G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Test strafing to left.
     */
    @Test
    public void test_StrafeLeft() throws InterruptedException {
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(12,-5,55.68f) ;
        var G = UUGoalLib.veryclose2DTo(null,dest) ;
        test_Goal(G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Test move straight forward, mixed with a bit strafing (the target is diagonal,
     * and we don't turn the agent).
     */
    @Test
    public void test_Forward() throws InterruptedException {
        // agent start location should be around: <10.119276,-5.0025,55.681934>
        // orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis

        Vec3 dest = new Vec3(8,-5,60f) ;
        var G = UUGoalLib.veryclose2DTo(null,dest) ;
        test_Goal(G) ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }
}
