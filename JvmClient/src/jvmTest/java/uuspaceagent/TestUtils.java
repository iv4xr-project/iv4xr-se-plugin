package uuspaceagent;

import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.controller.*;
import spaceEngineers.model.ToolbarLocation;

public class TestUtils {

    public static void console(String str) {
        System.out.println(str);
    }

    /**
     * For creating an SE-env, loading a gameworld into SE, then creating a test-agent bound to
     * the gameworld through the SE-env.
     */
    public static Pair<TestAgent, UUSeAgentState2D> loadSE(String worldName) {
        var agentId = "se0" ; // ""agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                //JsonRpcCharacterController.Companion.localhost(agentId),
                //JsonRpcSpaceEngineersBuilder.Companion.localhost(agentId),
                // WP: it should be this:
                //   JvmSpaceEngineersBuilder.Companion.default().localhost(agentId)
                // but rejected ... i think this is Intellij issue
                new SpaceEngineersJavaProxyBuilder().localhost(agentId),
                context
        ) ;

        console("** Loading the world " + worldName) ;
        var theEnv = new SeEnvironment( worldName,
                controllerWrapper,
                //context
                SeEnvironment.Companion.getDEFAULT_SCENARIO_DIR()
        ) ;
        theEnv.loadWorld() ;

        var myAgentState = new UUSeAgentState2D(agentId) ;

        console("** Creating a test-agent");
        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv) ;

        return new Pair<>(testAgent,myAgentState) ;
    }

    public static void closeConnectionToSE(UUSeAgentState state){
        try {
            state.env().getController().close();
        }
        catch (Exception e) {
            // swallow...
        }
    }


    public static Pair<TestAgent, UUSeAgentState3DOctree> loadSE3D(String worldName) {
        var agentId = "se0" ; // ""agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                //JsonRpcCharacterController.Companion.localhost(agentId),
                //JsonRpcSpaceEngineersBuilder.Companion.localhost(agentId),
                // WP: it should be this:
                //   JvmSpaceEngineersBuilder.Companion.default().localhost(agentId)
                // but rejected ... i think this is Intellij issue
                new SpaceEngineersJavaProxyBuilder().localhost(agentId),
                context
        ) ;

        console("** Loading the world " + worldName) ;
        var theEnv = new SeEnvironment( worldName,
                controllerWrapper,
                //context
                SeEnvironment.Companion.getDEFAULT_SCENARIO_DIR()
        ) ;
        theEnv.loadWorld() ;

        var myAgentState = new UUSeAgentState3DOctree(agentId) ;

        console("** Creating a test-agent");
        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv) ;

        return new Pair<>(testAgent, myAgentState) ;
    }

    public static Pair<TestAgent, UUSeAgentState3DVoxelGrid> loadSE3D2(String worldName) {
        var agentId = "se0" ; // ""agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                //JsonRpcCharacterController.Companion.localhost(agentId),
                //JsonRpcSpaceEngineersBuilder.Companion.localhost(agentId),
                // WP: it should be this:
                //   JvmSpaceEngineersBuilder.Companion.default().localhost(agentId)
                // but rejected ... i think this is Intellij issue
                new SpaceEngineersJavaProxyBuilder().localhost(agentId),
                context
        ) ;

        console("** Loading the world " + worldName) ;
        var theEnv = new SeEnvironment( worldName,
                controllerWrapper,
                //context
                SeEnvironment.Companion.getDEFAULT_SCENARIO_DIR()
        ) ;
        theEnv.loadWorld() ;

        var myAgentState = new UUSeAgentState3DVoxelGrid(agentId) ;

        console("** Creating a test-agent");
        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv) ;

        return new Pair<>(testAgent, myAgentState) ;
    }
}
