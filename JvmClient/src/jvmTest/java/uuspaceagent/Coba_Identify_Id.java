package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;

import static uuspaceagent.TestUtils.loadSE;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import org.junit.jupiter.api.Test;
import spaceEngineers.model.*;
import static uuspaceagent.TestUtils.loadSE;


public class Coba_Identify_Id {
    @Test
    public void test() throws InterruptedException {
        var state = loadSE("world-3 blocks").snd;

        state.updateState(state.agentId);

        System.out.println("** find the id of blocks");

        state.updateState(state.agentId);

        WorldEntity agentInfo = state.wom.elements.get(state.agentId);
        System.out.println("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        System.out.println("** Agent's info: " + PrintInfos.showWOMElements(state.wom));

        /*
        System.out.println("======");
        CharacterObservation cobs = state.env().getController().getObserver().observe() ;
        if(cobs.getTargetBlock() != null) {
            System.out.println("=== target block: " + cobs.getTargetBlock().getId());
        }
        */

        WorldEntity target = SEBlockFunctions.findClosestBlock(state.wom, "BasicAssembler", 10);
        String batteryId = target.id;
        System.out.println("** target state: " + PrintInfos.showWorldEntity(target));

        TestUtils.closeConnectionToSE(state);
    }
}
