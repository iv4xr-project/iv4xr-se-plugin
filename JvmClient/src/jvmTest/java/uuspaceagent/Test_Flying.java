package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;

import static nl.uu.cs.aplib.AplibEDSL.DEPLOYonce;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMAgent;
import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

public class Test_Flying {

    public Pair<TestAgent, UUSeAgentState2D> deployAgent() throws InterruptedException {
        var agentAndState = loadSE("myworld-3 with open door") ; // loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        UUSeAgentState2D state = agentAndState.snd ;
        Thread.sleep(1000);
        state.updateState(state.agentId);
        // agent start location
        // agent se0 @<10.22475,-5.0025,53.75382>, hdir:<-0.08024501,7.549446E-5,0.99677515>, vdir:<-4.629542E-5,1.0,-7.94657E-5>, health:1.0, jet:false
        console(showWOMAgent(state.wom));
        return new Pair<TestAgent, UUSeAgentState2D>(agent,state) ;
    }

    public void test_Goal(TestAgent agent, UUSeAgentState state, GoalStructure G) throws InterruptedException {
        agent.setGoal(G) ;
        int turn= 0 ;
        while(G.getStatus().inProgress()) {
            console(">> [" + turn + "] " + showWOMAgent(state.wom));
            agent.update();
            //Thread.sleep(50);
            turn++ ;
            if (turn >= 1400) break ;
        }
        TestUtils.closeConnectionToSE(state);
    }

    @Test
    public void test_flying_to_some_location() throws InterruptedException {
        console("*** start test...") ;
        //Thread.sleep(5000);
        Vec3 dest = new Vec3(10,-5,30) ;
        //Vec3 dest = new Vec3(10,5,53.7f) ;
        //Vec3 dest = new Vec3(25,-5,60) ;
        //Vec3 dest = new Vec3(10,3,56) ;
        var agentAndState = deployAgent();
        var agent = agentAndState.fst ;
        var state = agentAndState.snd ;
        state.navgrid.enableFlying = true ;
        GoalStructure G = DEPLOYonce(agent, UUGoalLib.closeTo(dest)) ;
        test_Goal(agentAndState.fst, agentAndState.snd, G) ;
        G.printGoalStructureStatus();
        console(("#### Remaining PATH to follow: " + PrintInfos.showPath(state,state.currentPathToFollow)));
        assertTrue(G.getStatus().success());
    }
}
