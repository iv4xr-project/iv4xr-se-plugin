package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import org.junit.jupiter.api.Test;
import spaceEngineers.controller.useobject.UseObjectExtensions;
import spaceEngineers.model.Block;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.DoorBase;
import spaceEngineers.model.ToolbarLocation;

import static uuspaceagent.TestUtils.loadSE;

/**
 * For trying out grinding directly using primitives from SE.
 */
public class Coba_DoorInteract {

    @Test
    public void test() throws InterruptedException {

        var state = loadSE("myworld-3 atdoor").snd;

        state.updateState(state.agentId);

        System.out.println("** Equiping grinder");
        state.env().equip(new ToolbarLocation(0,0));

        state.updateState(state.agentId);

        WorldEntity agentInfo = state.wom.elements.get(state.agentId) ;
        System.out.println("** Agent's info: " + PrintInfos.showWorldEntity(agentInfo));

        //System.out.println("==== " + PrintInfos.showWOMElements(state.wom));
        System.out.println("======");
        CharacterObservation cobs = state.env().getController().getObserver().observe() ;
        if(cobs.getTargetBlock() != null) {
            System.out.println("=== target block: " + cobs.getTargetBlock().getId());
        }
        WorldEntity target = SEBlockFunctions.findClosestBlock(state.wom, "LargeBlockSlideDoor", 10) ;
        String doorId = target.id ;
        System.out.println("** door state 1: " + PrintInfos.showWorldEntity(target));

        UseObjectExtensions useUtil = new UseObjectExtensions(state.env().getController().getSpaceEngineers()) ;

        Block targetBlock = state.env().getController().getObserver().observe().getTargetBlock() ;
        useUtil.openIfNotOpened((DoorBase) targetBlock);

        Thread.sleep(2000);
        state.updateState(state.agentId);

        target = SEBlockFunctions.findClosestBlock(state.wom, "LargeBlockSlideDoor", 10) ;
        //doorId = target.id ;
        System.out.println("** door state 2: " + PrintInfos.showWorldEntity(target));

    }
}
