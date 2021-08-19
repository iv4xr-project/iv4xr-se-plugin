package xxxx;

import UUspaceagent.USeAgentState;
import environments.SeAgentState;
import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.ContextControllerWrapper;
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.model.ToolbarLocation;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test_NavigateTo {

    //@Test
    public void test_0() {
        int x = 10 ;
        assertTrue(x == 10) ;
    }

    @Test
    public void test1() {
        System.out.println("*** start test...") ;
        var agentId = "agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                //JsonRpcCharacterController.Companion.localhost(agentId),
                JsonRpcSpaceEngineersBuilder.Companion.localhost(agentId),
                context
        ) ;

        var theEnv = new SeEnvironment( "myworld-3", // "simple-place-grind-torch-with-tools", //"iv4xr-uu-test1",
                controllerWrapper,
                context ) ;
        theEnv.loadWorld() ;

        var myAgentState = new USeAgentState(agentId) ;

        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv) ;
    }
}
