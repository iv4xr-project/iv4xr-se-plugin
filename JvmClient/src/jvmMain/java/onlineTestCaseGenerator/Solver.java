package onlineTestCaseGenerator;

import eu.iv4xr.framework.mainConcepts.Iv4xrAgentState;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.mainConcepts.SimpleState;
import uuspaceagent.SEBlockFunctions;
import uuspaceagent.UUGoalLib;

import java.util.function.Predicate;

import static nl.uu.cs.aplib.AplibEDSL.*;

public class Solver {
    Predicate<SimpleState> phi ;

    public GoalStructure use(TestAgent agent, String Type){
        if(Type.contains("Door")){
            return UUGoalLib.interacted(agent);
        }else if (Type.contains("Assembler")){
            return SEQ(UUGoalLib.targetBlockOK(agent, e ->
                                    Type.equals(e.getStringProperty("blockType"))
                                            && (float) e.getProperty("integrity") == (float) e.getProperty("maxIntegrity"),
                            false
                    ),
                    UUGoalLib.grinded(agent,0.1f),
                    UUGoalLib.targetBlockOK(agent, e ->
                                    (float) e.getProperty("integrity") <= 0.1f * (float) e.getProperty("maxIntegrity"),
                            false
                    ));
        }
        return null;
    }

    public GoalStructure navigateTo(TestAgent agent, String Type){
        if(Type.contains("Door")){
           return DEPLOY(agent, UUGoalLib.closeTo(agent,
                    Type,
                    SEBlockFunctions.BlockSides.BACK,
                    0.5f));
        } else if (Type.contains("Assembler")){
            return DEPLOY(agent, UUGoalLib.closeTo(agent,
                    Type,
                    SEBlockFunctions.BlockSides.FRONT,
                    0.5f));
    }
        return null;
    }

    public GoalStructure solver(TestAgent agent, Predicate<SimpleState> phi, String Type){
        this.phi = phi ;
        GoalStructure search =
                SEQ(
                        //select the item and navigate to it

                        navigateTo(agent, Type),
                        //use the item
                        use(agent,Type)
                );
        return
                IF(phi,
                        SUCCESS(), REPEAT(
                                search,phi)
                );

    }

    public GoalStructure solver(TestAgent agent, Predicate<SimpleState> phi, Vec3 position, String Type){
        this.phi = phi ;
        GoalStructure search =
                SEQ(
                        //select the item and navigate to it
                        DEPLOYonce(agent, UUGoalLib.closeToPosition(agent,
                                Type,
                                SEBlockFunctions.BlockSides.FRONT,
                                position,
                                20f,
                                0.5f)),
                        //use the item
                        use(agent,Type)

                );

        return REPEAT(
                search,phi
        );
    }
}
