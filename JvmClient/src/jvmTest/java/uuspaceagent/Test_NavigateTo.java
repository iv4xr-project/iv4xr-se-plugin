package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.* ;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.* ;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nl.uu.cs.aplib.AplibEDSL.* ;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

/**
 * Testing auto-navigation using the navigateTo tactic.
 */
public class Test_NavigateTo {

    /**
     * Auto-navigate to a given point.
     */
    public Pair<TestAgent,GoalStructure> test_navigateTo(Vec3 destination) throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        USeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState();

        // agent start location should be around: <10.119276,-5.0025,55.681934>
        //  orientationForward: <-0.043967947,-2.0614608E-4,0.9990329> ... so looking towards z-axis
        console(showWOMAgent(state.wom));

        var sqAgent = state.grid2D.gridProjectedLocation(state.wom.position) ;
        var sqDesitnation = state.grid2D.gridProjectedLocation(destination) ;

        GoalStructure G = goal("close to destination")
                .toSolve((USeAgentState st) -> {
                    var currentAgentSq = st.grid2D.gridProjectedLocation(st.wom.position) ;
                    return currentAgentSq.equals(sqDesitnation) ;
                })
                .withTactic(TacticLib.navigateTo(destination))
                .lift() ;

        agent.setGoal(G) ;

        int turn= 0 ;
        while(G.getStatus().inProgress()) {
            console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            Thread.sleep(50);
            turn++ ;
            if (turn >= 1400) break ;
        }
        return new Pair<>(agent,G) ;
    }

    /**
     * Destination lies in a straight and clear line from the agent.
     */
    //@Test
    public void test1() throws InterruptedException {
        // navigating to (10,-5,65) ... this is just before the buttons-panel
        Vec3 dest = new Vec3(10,-5,65) ;
        var agent_and_goal = test_navigateTo(dest) ;
        var G = agent_and_goal.snd ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }

    /**
     * Reachable, but the agent will have to bend a bit.
     */
    @Test
    public void test2() throws InterruptedException {
        // navigating to (10,-5,65) ... this is just before the buttons-panel
        Vec3 dest = new Vec3(10,-5,73) ;
        var agent_and_goal = test_navigateTo(dest) ;
        var G = agent_and_goal.snd ;
        G.printGoalStructureStatus();
        assertTrue(G.getStatus().success());
    }



}
