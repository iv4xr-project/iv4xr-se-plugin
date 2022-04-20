package spaceEngineers.iv4xr;

import environments.SeAgentState;
import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import nl.uu.cs.aplib.mainConcepts.*;

import static environments.SeEnvironmentKt.toWorldModel;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import static eu.iv4xr.framework.Iv4xrEDSL.* ;
import static uuspaceagent.TestUtils.console;

import org.junit.jupiter.api.Test;
import spaceEngineers.controller.ContextControllerWrapper;
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.iv4xr.goal.TacticLib;
import spaceEngineers.model.*;

public class Test_SimpleInteractionsWithSE {

    //@Test
    public void example_of_deploying_a_goal_to_SE() throws InterruptedException {
        console("*** start test...") ;
        var agentId = "agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                new SpaceEngineersJavaProxyBuilder().localhost(agentId),
                context
        ) ;

        var theEnv = new SeEnvironment("iv4xr-uu-test1", controllerWrapper) ;
        theEnv.loadWorld() ;
        theEnv.observeForNewBlocks() ;

        var dataCollector = new TestDataCollector() ;
        var myAgentState = new SeAgentState(agentId) ;

        var testAgent = new TestAgent(agentId, "some role name, else nothing")
                .attachState(myAgentState)
                .attachEnvironment(theEnv)
                .setTestDataCollector(dataCollector) ;

        var tactics = new TacticLib() ;

        // set a goal ... does not matter what. This one cannot be solved.
        GoalStructure G = goal("dummy").toSolve((SeAgentState S) -> false)
                .withTactic(tactics.moveForward())
                .lift();
        testAgent.setGoal(G) ;

        int i = 0 ;
        while(i<200) {
            testAgent.update();
            console("*** " + i + " " + myAgentState.worldmodel.agentId +  " @" + myAgentState.worldmodel.position) ;
            if(i==2) {
                // debug here...
            }
            Thread.sleep(10);
            i++ ;
        }
    }

    double angle(double x, double z) {
        //double epsilon = 0.0001 ;
        //if(Math.abs(x) <= epsilon) {
        //    if(z>=0) return 0 ;
        //    return 180 ;
        //}
        double angle =  Math.toDegrees(Math.atan(z/x)) ;
        return angle ;
    }

    @Test
    public void test_env_interactions() throws InterruptedException {
        console("*** start test...") ;
        var agentId = "agentId" ;
        var blockType = "LargeHeavyBlockArmorBlock" ;
        var context = new SpaceEngineersTestContext() ;
        context.getBlockTypeToToolbarLocation().put(blockType, new ToolbarLocation(1, 0))  ;

        var controllerWrapper = new ContextControllerWrapper(
                new SpaceEngineersJavaProxyBuilder().localhost(agentId),
                context
        ) ;

        var theEnv = new SeEnvironment( "myworld-3", // "simple-place-grind-torch-with-tools", //"iv4xr-uu-test1",
                controllerWrapper) ;
        theEnv.loadWorld() ;
        theEnv.observeForNewBlocks() ;

        Thread.sleep(10);
        //theEnv.getController().getCharacter().moveAndRotate(new Vec3(0,0,0), new Vec2(0,90),0) ;
        WorldModel obs = theEnv.observe() ;
        CharacterObservation primObs = theEnv.getController().getObserver().observe() ;
        var hdir =  primObs.getOrientationForward() ;
        obs = theEnv.observe() ;
        console(">> pos :" + obs.position) ;
        console(">> hdir:" + angle(hdir.getX(), hdir.getZ())) ;
        console(">> vdir:" + primObs.getOrientationUp()) ;
        console(">> velo:" + obs.velocity) ;

        int i = 0 ;
        while(i<20) {
            obs = theEnv.moveForward() ;
            if (i<20) {
                theEnv.getController().getCharacter().moveAndRotate(new Vec3F(0,0,0), new Vec2F(0,20),0, 1) ;
            }
            else {
                theEnv.getController().getCharacter().moveAndRotate(new Vec3F(0,0,-36), new Vec2F(0,0),0, 1) ;
            }
            obs = theEnv.observe() ;
            primObs = theEnv.getController().getObserver().observe() ;
            hdir =  primObs.getOrientationForward() ;
            console(">> pos :" + obs.position) ;
            console(">> extent :" + primObs.getExtent()) ;
            console(">> hdir:" + angle(hdir.getX(), hdir.getZ())) ;
            console(">> hdir:" + primObs.getOrientationForward()) ;
            console(">> vdir:" + primObs.getOrientationUp()) ;
            console(">> velo:" + obs.velocity) ;
            Thread.sleep(10);
            i++ ;
        }
        //theEnv.getController().getCharacter().moveAndRotate()
        // printing wom:

        Observation primObsX = theEnv.getController().getObserver().observeBlocks() ;
        obs = toWorldModel(primObsX) ; // static method...

        console("\n\nWOM: #elements " + obs.elements.size())  ;
        console("Raw obs #grid-elems " + primObsX.getGrids().size())  ;

        boolean printBlockInfo = true ;

        if (!printBlockInfo) return ;

        i = 0 ;
        for(var e : obs.elements.values()) {
             console(" === Entity " + i + ": " + e.id + " (" + e.type + ")") ;
             console("     Pos: " + e.position) ;
             for(var prop : e.properties.entrySet()) {
                 console("    " + prop.getKey() + ": " + prop.getValue()) ;
             }
             int k = 0 ;
             for (var f : e.elements.values()) {
                 console("       >> Sub: " + k + ": " + f.id + " (" + f.type + ")") ;
                 console("          Pos:    " + f.position) ;
                 console("          Extent: " + f.extent) ;
                 for(var prop : f.properties.entrySet()) {
                     console("          " + prop.getKey() + ": " + prop.getValue()) ;
                 }
                 k++ ;
             }
             i++ ;
        }

    }


}
