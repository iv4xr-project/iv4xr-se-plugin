package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.useobject.UseObjectExtensions;
import spaceEngineers.model.*;

import static uuspaceagent.TestUtils.loadSE;

/**
 * For trying out use-block primitive from SE.
 */
public class Coba_UseObject {

    @Test
    public void test() throws InterruptedException {

        var state = loadSE("myworld-3 with open door X").snd;

        state.useSystemTimeForTimeStamping = true ;

        state.updateState(state.agentId);

        WorldEntity agentInfo = state.worldmodel.elements.get(state.agentId) ;
        System.out.println("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));
        WorldEntity target = SEBlockFunctions.findClosestBlock(state.worldmodel, "SurvivalKitLarge", 10) ;
        System.out.println("** target: " + PrintInfos.showWorldEntity(target));

        // Using a survival-kit to heal the agent. Making use of the
        // admin-feature-shortcut:
        for (int k=0; k<100; k++) {
            // run a loop to heal
            state.env().getController().getAdmin().getCharacter().use(target.id, 0,
                    UseObject.Companion.getManipulate());
            //Thread.sleep(20);
            state.updateState(state.agentId);
            float h = (Float) state.val("health") ;
            float h0 = (Float) state.before("health") ;
            agentInfo = state.get(state.agentId) ;
            long t = agentInfo.timestamp ;
            long t0 = agentInfo.getPreviousState().timestamp ;
            System.out.println(">>> healing: " + (h - h0) + "/" + (t - t0));
        }

        Thread.sleep(2000);

        // for closing the Terminal-screen, if you experiment with
        // an action that open a terminal (above)
        /*
        try {

            state.env().getController().getScreens().getTerminal().close();
        }
        catch(Exception e) {
            // the terminal was not open
        }
        */

        state.updateState(state.agentId);
        agentInfo = state.worldmodel.elements.get(state.agentId) ;
        System.out.println("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

    }
}
