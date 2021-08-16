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
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.ContextControllerWrapper;
//import spaceEngineers.controller.JsonRpcCharacterController;
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder;
import spaceEngineers.controller.SpaceEngineersTestContext;
import spaceEngineers.iv4xr.goal.TacticLib;
import spaceEngineers.model.*;

public class Test_SimpleInteractionsWithSE {

    //@Test
    public void example_of_deploying_a_goal_to_SE() throws InterruptedException {
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

        var theEnv = new SeEnvironment("iv4xr-uu-test1",
                controllerWrapper,
                context ) ;
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
                .withTactic(tactics.moveForward(1f))
                .lift()
                ;
        testAgent.setGoal(G) ;

        int i = 0 ;
        while(i<200) {
            testAgent.update();
            System.out.println("*** " + i + " " + myAgentState.wom.agentId +  " @" + myAgentState.wom.position) ;
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
        theEnv.observeForNewBlocks() ;

        Thread.sleep(10);
        //theEnv.getController().getCharacter().moveAndRotate(new Vec3(0,0,0), new Vec2(0,90),0) ;
        WorldModel obs = theEnv.observe() ;
        CharacterObservation primObs = theEnv.getController().getObserver().observe() ;
        var hdir =  primObs.getOrientationForward() ;
        obs = theEnv.observe() ;
        System.out.println(">> pos :" + obs.position) ;
        System.out.println(">> hdir:" + angle(hdir.getX(), hdir.getZ())) ;
        System.out.println(">> vdir:" + primObs.getOrientationUp()) ;
        System.out.println(">> velo:" + obs.velocity) ;

        int i = 0 ;
        while(i<12) {
            //obs = theEnv.moveForward(5f) ;
            theEnv.getController().getCharacter().moveAndRotate(new Vec3(0,0,0), new Vec2(0,40),0) ;
            //theEnv.getController().getCharacter().moveAndRotate(new Vec3(5,0,0), new Vec2(0,1),0) ;
            obs = theEnv.observe() ;
            primObs = theEnv.getController().getObserver().observe() ;
            hdir =  primObs.getOrientationForward() ;
            System.out.println(">> pos :" + obs.position) ;
            System.out.println(">> hdir:" + angle(hdir.getX(), hdir.getZ())) ;
            System.out.println(">> hdir:" + primObs.getOrientationForward()) ;
            System.out.println(">> vdir:" + primObs.getOrientationUp()) ;
            System.out.println(">> velo:" + obs.velocity) ;
            Thread.sleep(10);
            i++ ;
        }
        //theEnv.getController().getCharacter().moveAndRotate()
        // printing wom:

        Observation primObsX = theEnv.getController().getObserver().observeBlocks() ;
        obs = toWorldModel(primObsX) ; // static method...

        System.out.println("\n\nWOM: #elements " + obs.elements.size())  ;
        System.out.println("     #grid-elems " + primObsX.getGrids().size())  ;


        i = 0 ;
        for(var e : obs.elements.values()) {
             System.out.println(" === Entity " + i + ": " + e.id + " (" + e.type + ")") ;
             System.out.println("     Pos: " + e.position) ;
             for(var prop : e.properties.entrySet()) {
                 System.out.println("    " + prop.getKey() + ": " + prop.getValue()) ;
             }
             int k = 0 ;
             for (var f : e.elements.values()) {
                 System.out.println("       >> Sub: " + k + ": " + f.id + " (" + f.type + ")") ;
                 System.out.println("          Pos:    " + f.position) ;
                 System.out.println("          Extent: " + f.extent) ;
                 for(var prop : f.properties.entrySet()) {
                     System.out.println("          " + prop.getKey() + ": " + prop.getValue()) ;
                 }
                 k++ ;
             }
             i++ ;
        }

    }


}
