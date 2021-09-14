package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

import static uuspaceagent.TestUtils.console;
import static uuspaceagent.TestUtils.loadSE;

/**
 * For trying out flying. Primarily using second-tier actions provided by TacticLib
 * (so, not by directly invoking primitive SE methods).
 */
public class Coba_Flying {

   // @Test
    public void test1() throws InterruptedException {

        var state = loadSE("myworld-3").snd;

        state.updateState();

        System.out.println("** Trying to FLY");

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.env().getController().getCharacter().turnOnJetpack() ;

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        System.out.println("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        int k = 0 ;
        while (k<100) {
            state.updateState();
            console("** k=" + k + ", agent: " + PrintInfos.showWOMAgent(state.wom)) ;
            state.env().getController().getCharacter().moveAndRotate(
                    new spaceEngineers.model.Vec3(0,0.3,0),
                    new spaceEngineers.model.Vec2(0,0),
                    0) ;
            k++ ;
        }
    }

    void moveTo(UUSeAgentState state, Vec3 destination) {
        state.updateState();
        for (int k = 0 ; k<20; k++) {
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
    public void testFlyDifferentDirections() throws InterruptedException {

        var state = loadSE("myworld-3").snd;

        state.updateState();

        System.out.println("** Trying to FLY");

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.env().getController().getCharacter().turnOnJetpack() ;
        state.updateState();

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        console("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        // agent se0 @<9.549925,-5.0025005,54.149185>, hdir:<-0.0064151124,1.6736684E-4,0.99997944>, vdir:<-3.9837923E-5,1.0,-1.6762585E-4>, health:1.0, jet:true

        //Vec3 destination = new Vec3(11.5f,-1.5f,60f) ;
        console("####  Moving up:");
        state.updateState();
        Vec3 destination = new Vec3(9.54f,3f,54.2f) ;
        moveTo(state, destination) ;


        console("####  Moving to z+:");
        state.updateState();
        destination = new Vec3(9.54f,3f,60f) ;
        moveTo(state, destination) ;

        console("####  Moving to z-:");
        state.updateState();
        destination = new Vec3(9.54f,3f,54.2f) ;
        moveTo(state, destination) ;

        console("####  Moving to x-:");
        state.updateState();
        destination = new Vec3(5f,3f,45f) ;
        moveTo(state, destination) ;

        console("####  Moving to x+:");
        state.updateState();
        destination = new Vec3(9.54f,3f,54.2f) ;
        moveTo(state, destination) ;

        console("####  Moving down:");
        state.updateState();
        destination = new Vec3(9.54f,-5f,54.2f) ;
        moveTo(state, destination) ;
    }

    @Test
    public void testFlyAndYrotate() throws InterruptedException {
        var state = loadSE("myworld-3").snd;

        state.updateState();

        System.out.println("** Trying to FLY");

        state.updateState();
        state.navgrid.enableFlying = true ;
        state.env().getController().getCharacter().turnOnJetpack() ;
        state.updateState();

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        console("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));
        console("** Agent @ " + state.wom.position);

        //Thread.sleep(5000) ;


        // agent se0 @<9.549925,-5.0025005,54.149185>, hdir:<-0.0064151124,1.6736684E-4,0.99997944>, vdir:<-3.9837923E-5,1.0,-1.6762585E-4>, health:1.0, jet:true

        //Vec3 destination = new Vec3(11.5f,-1.5f,60f) ;
        console("####  Moving up:");
        state.updateState();
        Vec3 destination = new Vec3(9.54f,4f,54.2f) ;
        moveTo(state, destination) ;

        state.updateState();
         destination = new Vec3(9.54f,1f,65f) ;
        moveTo(state, destination) ;

        state.updateState();
        destination = new Vec3(9.54f,4f,54.2f) ;
        moveTo(state, destination) ;

        // rotate
        console("####  rotating around Y:");
        state.updateState();
        console("** Agent @ " + state.wom.position);
        destination = new Vec3(9.54f,4f,50f) ;
        UUTacticLib.yTurnTowardACT(state, destination, 0.98f, 400) ;

        state.updateState();
        console("** Agent @ " + state.wom.position);
    }
}
