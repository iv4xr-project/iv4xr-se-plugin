package uuspaceagent;

import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import nl.uu.cs.aplib.utils.Pair;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.ContextControllerWrapper;
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.model.ToolbarLocation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.PrintInfos.showWOMElements;
import static uuspaceagent.SEBlockFunctions.findWorldEntity;

public class Test_NavigateTo {

    //@Test
    public void test_0() {
        int x = 10 ;
        assertTrue(x == 10) ;
    }

    void console(String str) {
        System.out.println(str);
    }

    /**
     * For creating an SE-env, loading a gameworld into SE, then creating a test-agent bound to
     * the gameworld through the SE-env.
     */
    Pair<TestAgent,USeAgentState> loadSE(String worldName) {
        var agentId = "agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                //JsonRpcCharacterController.Companion.localhost(agentId),
                JsonRpcSpaceEngineersBuilder.Companion.localhost(agentId),
                context
        ) ;

        console("** Loading the world " + worldName) ;
        var theEnv = new SeEnvironment( worldName, controllerWrapper, context ) ;
        theEnv.loadWorld() ;

        var myAgentState = new USeAgentState(agentId) ;

        console("** Creating a test-agent");
        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv) ;

        return new Pair<>(testAgent,myAgentState) ;
    }

    @Test
    public void test1() throws InterruptedException {
        console("*** start test...") ;
        var agentAndState = loadSE("myworld-3")  ;
        TestAgent agent = agentAndState.fst ;
        USeAgentState state = agentAndState.snd ;
        Thread.sleep(1000);
        // do a single update, and check that we if we have the structures:
        state.updateState();
        assertTrue(state.grid2D.allObstacleIDs.size() > 0 ) ;
        System.out.println(showWOMElements(state.wom)) ;
        System.out.println("=========\n") ;
        System.out.println("#obstacles:" + state.grid2D.allObstacleIDs.size()) ;

        for(var o : state.grid2D.allObstacleIDs) {
            WorldEntity we = findWorldEntity(state.wom,o) ;
            System.out.println("  Obs: " + o + " (" + we.properties.get("blockType") + ")");
        }
    }
}
