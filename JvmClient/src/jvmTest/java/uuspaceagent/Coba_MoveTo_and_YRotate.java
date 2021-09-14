package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

public class Coba_MoveTo_and_YRotate {

    void moveTo(UUSeAgentState state, Vec3 destination) {
        state.updateState();
        for (int k = 0 ; k<40; k++) {
            UUTacticLib.moveToward(state, destination,10) ;
            state.updateState();
            float distance = Vec3.sub(destination,state.wom.position).length() ;
            console(">>> dist to dest: " + distance);
            if(distance <= 0.5) {
                break ;
            }
        }
    }

    //@Test
    public void test_2DMoveTo1() throws InterruptedException {

        var state = loadSE("myworld-3").snd;

        state.updateState();

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.updateState();

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        console("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        // agent se0 @<9.549925,-5.0025005,54.149185>, hdir:<-0.0064151124,1.6736684E-4,0.99997944>, vdir:<-3.9837923E-5,1.0,-1.6762585E-4>, health:1.0, jet:true

        //Vec3 destination = new Vec3(11.5f,-1.5f,60f) ;
        console("####  to z+");
        state.updateState();
        Vec3 destination = new Vec3(9.54f,-5f,60f) ;
        moveTo(state, destination) ;

        console("####  Moving to z-:");
        state.updateState();
        destination = new Vec3(9.54f,-5f,54.2f) ;
        moveTo(state, destination) ;
    }

    //@Test
    public void test_2DMoveTo2() throws InterruptedException {

        var state = loadSE("myworld-3").snd;

        state.updateState();

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.updateState();

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        console("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        // agent se0 @<9.549925,-5.0025005,54.149185>, hdir:<-0.0064151124,1.6736684E-4,0.99997944>, vdir:<-3.9837923E-5,1.0,-1.6762585E-4>, health:1.0, jet:true

        console("####  to x+");
        state.updateState();
        Vec3 destination = new Vec3(10.8f,-5f,54.14f) ;
        moveTo(state, destination) ;

        console("####  Moving to z-:");
        state.updateState();
        destination = new Vec3(9.54f,-5f,54.14f) ;
        moveTo(state, destination) ;
    }

    @Test
    public void test_YTurn() throws InterruptedException {

        var state = loadSE("myworld-3").snd;

        state.updateState();

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.updateState();

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        console("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        // agent se0 @<9.549925,-5.0025005,54.149185>, hdir:<-0.0064151124,1.6736684E-4,0.99997944>, vdir:<-3.9837923E-5,1.0,-1.6762585E-4>, health:1.0, jet:true

        console("####  turning to left +x");
        state.updateState();
        Vec3 destination = new Vec3(12f,-5f,54.15f) ;
        UUTacticLib.yTurnTowardACT(state,destination,0.99f,200) ;

        console("####  turning to left -x");
        state.updateState();
        destination = new Vec3(0f,-5f,54.15f) ;
        UUTacticLib.yTurnTowardACT(state,destination,0.99f,200) ;


    }
}
