package uuspaceagent;

import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.controller.ContextControllerWrapper;
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.model.ToolbarLocation;

public class TestUtils {

    public static void console(String str) {
        System.out.println(str);
    }

    /**
     * For creating an SE-env, loading a gameworld into SE, then creating a test-agent bound to
     * the gameworld through the SE-env.
     */
    public static Pair<TestAgent,USeAgentState> loadSE(String worldName) {
        var agentId = "se0" ; // ""agentId" ;
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
}