package uuspaceagent;

import environments.SeEnvironmentKt;
import eu.iv4xr.framework.mainConcepts.ObservationEvent;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.*;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Observation;
import spaceEngineers.model.ToolbarLocation;


import java.util.function.Function;
import java.util.function.Predicate;

public class UUGoalLib {

    /**
     * A goal that is solved when the agent manage to be in some distance close to a
     * given destination. The destination itself should be reachable from the agent
     * current position. The solver for this goal is the tactic navigateToTAC.
     *
     * The goal is aborted if the destination is not reachable.
     */
    public static Function<UUSeAgentState,GoalStructure> closeTo(String goalname, Vec3 targetLocation) {

        if(goalname == null) {
            goalname = "close to location " + targetLocation ;
        }

        String goalname_ = goalname ;

        return (UUSeAgentState state) -> {
            Vec3 targetSquareCenter = state.navgrid.getSquareCenterLocation(state.navgrid.gridProjectedLocation(targetLocation));
            GoalStructure G = goal(goalname_)
                    .toSolve((Pair<Vec3,Vec3> posAndOrientation) -> {
                        var agentPosition = posAndOrientation.fst ;
                        return Vec3.sub(targetSquareCenter,agentPosition).lengthSq() <= UUTacticLib.THRESHOLD_SQUARED_DISTANCE_TO_SQUARE ;
                    })
                    .withTactic(
                       FIRSTof(UUTacticLib.navigateToTAC(targetLocation), ABORT()) )
                    .lift() ;
                 return G ;

        } ;
    }

    public static Function<UUSeAgentState,GoalStructure> closeTo(Vec3 targetLocation) {
        return closeTo(null,targetLocation) ;
    }

    /**
     * A goal that is solved when the agent manage to get close (the distance is specified by delta) to the center of a
     * specified face (front/back/ left/right) of the nearest block of the specified block-type, within the given
     * radius. The goal fails if there is no such block in the given radius, or if the agent cannot find a path
     * to the closest one.
     *
     * NOTE: for now the block should be a cube, and upright.
     */
    public static Function<UUSeAgentState,GoalStructure> closeTo(TestAgent agent,
                                                                 String blockType,
                                                                 SEBlockFunctions.BlockSides side,
                                                                 float radius,
                                                                 float delta) {
        float sqradius = radius * radius ;

        return closeTo(agent,
                "type " + blockType,
                (UUSeAgentState state) -> (WorldEntity e)
                        ->
                        blockType.equals(e.getStringProperty("blockType"))
                        && Vec3.sub(e.position, state.wom.position).lengthSq() <= sqradius,
                side,
                delta
                ) ;
    }

    /**
     * Use this to target a block using a generic selector function.
     */
    public static Function<UUSeAgentState,GoalStructure> closeTo(TestAgent agent,
                                                                 String selectorDesc,
                                                                 Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                 SEBlockFunctions.BlockSides side,
                                                                 float delta) {


        return  (UUSeAgentState state) -> {

            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom,selector.apply(state)) ;
            if (block == null) return FAIL("Navigating autofail; no block can be found: " + selectorDesc) ;

            Vec3 intermediatePosition = SEBlockFunctions.getSideCenterPoint(block,side,delta + 1.5f) ;
            Vec3 goalPosition = SEBlockFunctions.getSideCenterPoint(block,side,delta) ;
            Vec3 blockCenter = (Vec3) block.getProperty("centerPosition") ;

            // because the agent's position is actually its feet position, we take the corresponding
            // positions at the base of the block as goals. So we project goalPosition and intermediatePosition
            // above to positions at the base of the block.
            Vec3 size = SEBlockFunctions.getActualSize(block) ;
            intermediatePosition.y -= size.y * 0.5 ;
            goalPosition.y -= size.y * 0.5 ;

            return SEQ(DEPLOYonce(agent,
                            closeTo("close to a block of property " + selectorDesc + " @"
                            + block.position
                            + " ," + side + ", targeting " + intermediatePosition,
                            intermediatePosition)),
                      veryclose2DTo("very close to a block of property " + selectorDesc + " @"
                              + block.position
                              + " ," + side + ", targeting " + goalPosition,
                              goalPosition),
                      face2DToward("facing towards a block of property " + selectorDesc + " @"
                              + block.position
                              + " ," + side, blockCenter)
                    ) ;
        } ;
    }

    public static GoalStructure grinderEquiped() {
        return lift("Grinder equiped",
                  action("equip grinder").do1((UUSeAgentState state) -> {
                     state.env().equip(new ToolbarLocation(0,0));
                     return true ;
                  })
                ) ;
    }

    public static GoalStructure barehandEquiped() {
        return lift("Grinder equiped",
                action("equip grinder").do1((UUSeAgentState state) -> {
                    state.env().equip(new ToolbarLocation(0,9));
                    return true ;
                })
        ) ;
    }

    public static GoalStructure photo(String fname) {
        return lift("Screenshot made",
                action("Snapping a picture").do1((UUSeAgentState state) -> {
                    state.env().getController().getObserver().takeScreenshot(fname);
                    return true ;
                })
        ) ;
    }


    public static GoalStructure targetBlockOK(TestAgent agent, Predicate<WorldEntity> predicate, boolean abortIfFail) {

        Tactic checkAction = action("checking a predicate")
                .do1(state -> true)
                .on_((UUSeAgentState state) -> {
                    WorldEntity target = state.targetBlock() ;
                    boolean ok = true ;
                    if (target == null) {
                        ok = false ;
                    }
                    else {
                        ok = predicate.test(target) ;
                    }
                    var datacollector = agent.getTestDataCollector() ;
                    if (datacollector != null) {
                        ObservationEvent.VerdictEvent verdict = new ObservationEvent.VerdictEvent(
                                "Checking target block",
                                target == null ? "target is null" : "" + target.type + "@" + target.position,
                                ok
                                ) ;
                        datacollector.registerEvent(agent.getId(),verdict);
                    }
                    return ok ;
                })
                .lift() ;

        Tactic success = action("success").do1((UUSeAgentState state) -> true).lift() ;

        return SEQ(
           grinderEquiped(),
           goal("target entity passes a check")
                   .toSolve(b -> true)
                   .withTactic(
                      abortIfFail ? FIRSTof(checkAction,ABORT()) : FIRSTof(checkAction,success))
                   .lift(),
           barehandEquiped()
        ) ;
    }

    public static GoalStructure grinded(TestAgent agent, float targetIntegrity) {

        GoalStructure grind = DEPLOYonce(agent, (UUSeAgentState state) -> {
            WorldEntity target = state.targetBlock() ;
            if(target == null) {
                return FAIL("Grinding autofail: there is no target block.") ;
            }
            String targetId = target.id ;
            float precentageTagetIntegrity = 100 * targetIntegrity ;
            float integrityThreshold = ((float) target.getProperty("maxIntegrity")) * targetIntegrity ;

            return goal("block " + targetId + "(" + target.getStringProperty("blockType") + ") is grinded to integrity <= " + precentageTagetIntegrity + "%")
                        .toSolve((WorldEntity e) -> e == null || ((float) e.getProperty("integrity") <= integrityThreshold))
                        .withTactic(action("Grinding")
                                .do1((UUSeAgentState st) -> {
                                    UUTacticLib.grind(state,50);
                                    Observation rawGridsAndBlocksStates = st.env().getController().getObserver().observeBlocks() ;
                                    WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates) ;
                                    return SEBlockFunctions.findWorldEntity(st.wom,targetId) ;
                                })
                        .lift())
                    .lift() ;
        }) ;

        GoalStructure stopGrinding = lift("Grinding stopped",
                action("stop grinding")
                     .do1((UUSeAgentState st) -> {
                         st.env().endUsingTool();
                         return true ;
                      })
                ) ;

        return SEQ(grinderEquiped(), grind, stopGrinding, barehandEquiped()) ;
    }


    public static GoalStructure veryclose2DTo(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "very close to " + p ;
        }
        return goal(goalname)
                .toSolve((Float square_distance) -> {
                    //System.out.println(">> sq-dist " + square_distance) ;
                    return square_distance <= UUTacticLib.THRESHOLD_SQUARED_DISTANCE_TO_POINT ;
                })
                .withTactic(FIRSTof(UUTacticLib.straightline2DMoveTowardsACT(p).lift() , ABORT()))
                .lift() ;
    }

    public static GoalStructure face2DToward(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "face towards " + p ;
        }
        return goal(goalname)
                .toSolve((Float cos_alpha) -> 1 - cos_alpha <= 0.01)
                .withTactic(FIRSTof(UUTacticLib.yTurnTowardACT(p).lift() , ABORT()))
                .lift() ;
    }

}
