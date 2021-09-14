package uuspaceagent;

import org.junit.jupiter.api.Test;
import spaceEngineers.model.Vec2;
import spaceEngineers.model.Vec3;

import static uuspaceagent.TestUtils.loadSE;

/**
 * For experimenting with movement speed; we use SE primitive.
 */
public class Coba_MoveAndRotateSpeed {


    @Test
    public void test() throws InterruptedException {

        var state = loadSE("myworld-3").snd ;
        int k=0 ;
        while (k<200) {
            //state.env().moveForward(0.3f) ;
            //state.env().getController().getCharacter().moveAndRotate(new Vec3(-0.3,0,-0.3), new Vec2(0,0), 0) ;
            state.env().getController().getCharacter().moveAndRotate(new Vec3(0,0,0), new Vec2(0,10), 0) ;
            /*
            for(int burst=0; burst<10; burst++) {
                state.env().getController().getCharacter().moveAndRotate(new Vec3(0,0,-0.4), new Vec2(0,0), 0) ;
            }

             */
            //Thread.sleep(50);
            k++ ;
        }

    }

}
