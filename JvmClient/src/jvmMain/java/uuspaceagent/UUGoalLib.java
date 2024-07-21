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
import spaceEngineers.controller.useobject.UseObjectExtensions;
import spaceEngineers.model.Block;
import spaceEngineers.model.Observation;
import spaceEngineers.model.ToolbarLocation;


import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "rawtypes"})
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
            Vec3 targetSquareCenter = state.getBlockCenter(targetLocation); //state.navgrid.getSquareCenterLocation(state.navgrid.gridProjectedLocation(targetLocation));
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

    public static Function<UUSeAgentState,GoalStructure> closeToBlock(String targetBlockName,
                                                                      Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                      SEBlockFunctions.BlockSides side,
                                                                      float delta) {

        String goalname_ = "close to block " + targetBlockName;

        return (UUSeAgentState state) -> {
            GoalStructure G = goal(goalname_)
                    .toSolve((Pair<Vec3,Vec3> posAndOrientation) -> {
                        WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state));
                        if (block == null) return false;

                        Vec3 targetLocation = SEBlockFunctions.getSideCenterPoint(block, side, delta + 1.5f);
                        Vec3 targetSquareCenter = state.getBlockCenter(targetLocation);

                        var agentPosition = posAndOrientation.fst ;
                        return Vec3.sub(targetSquareCenter, agentPosition).lengthSq() <= UUTacticLib.THRESHOLD_SQUARED_DISTANCE_TO_SQUARE ;
                    })
                    .withTactic(
                            FIRSTof(UUTacticLib.navigateToBlockTAC(selector, side, delta), ABORT()) )
                    .lift() ;
            return G ;
        } ;
    }

    public static Function<UUSeAgentState,GoalStructure> exploreTo(String goalname, Vec3 targetLocation) {

        if(goalname == null) {
            goalname = "explore to location " + targetLocation ;
        }

        String goalname_ = goalname ;

        return (UUSeAgentState state) -> {
            Vec3 targetSquareCenter = targetLocation;
            GoalStructure G = goal(goalname_)
                    .toSolve((Pair<Vec3,Vec3> posAndOrientation) -> {
                        var agentPosition = posAndOrientation.fst ;
                        return Vec3.sub(targetSquareCenter,agentPosition).lengthSq() <= 6.2f;
                    })
                    .withTactic(
                            FIRSTof(UUTacticLib.exploreTAC(targetLocation),
                                    UUTacticLib.navigateToTAC(targetLocation),
                                    ABORT()) )
                    .lift() ;
            return G ;

        } ;
    }

    public static Function<UUSeAgentState,GoalStructure> closeToButton(int buttonNr) {

        return (UUSeAgentState state) -> {
            if (state.doors.isEmpty()) return FAIL();
            if (state.buttons.isEmpty()) return FAIL();

            WorldEntity button = (WorldEntity) state.buttons.get(buttonNr);

            Vec3 blockCenter = (Vec3) button.getProperty("centerPosition");
            Vec3 buttonPanelCenter = blockCenter.copy();
            buttonPanelCenter = Vec3.add(buttonPanelCenter, (Vec3) button.getProperty("orientationForward"));
            buttonPanelCenter = Vec3.sub(buttonPanelCenter, Vec3.mul((Vec3) button.getProperty("orientationUp"), 0.5f));

            return SEQ(
                    closeTo("close to block ButtonPanelLarge", blockCenter).apply(state),
                    veryclose2DTo("very close to block ButtonPanelLarge", blockCenter),
                    face2DToward("facing towards block ButtonPanelLarge", buttonPanelCenter)
                    //face2DTowardUpDown("facing* towards block ButtonPanelLarge", buttonPanelCenter)
            );
        };
    }

    /**
     * Create a pressButton goal that presses the buttonNR button on the buttonID button-panel.
     * @param buttonID The index of the button in the state's buttons list
     * @param buttonNR The button to press on the button-panel [1,2,3,4]
     */
    public static GoalStructure pressButton(int buttonID, int buttonNR) {
        return lift((UUSeAgentState S) -> {
            UseObjectExtensions useUtil = new UseObjectExtensions(S.env().getController().getSpaceEngineers());
            WorldEntity button = (WorldEntity) S.buttons.get(buttonID);
            if (button == null) return false;
            Block targetBlock = S.env().getBlock(button.id); //S.env().getController().getObserver().observe().getTargetBlock();
            useUtil.pressButton(targetBlock, buttonNR);
            S.updateDoors();
            return true;
        });
    }

    public static Function<UUSeAgentState,GoalStructure> faceTowardBlock(String goalname,
                                                                         Function<UUSeAgentState, Predicate<WorldEntity>> selector) {
         return (UUSeAgentState state) -> {
            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state));
            if (block == null) return FAIL("Block not found");
            Vec3 pos = block.position;
            return goal(goalname)
                    .toSolve((Float cos_alpha) -> 1 - cos_alpha <= 0.01)
                    .withTactic(FIRSTof(UUTacticLib.yTurnTowardACT(pos).lift() , ABORT()))
                    .lift() ;
        };
    }

    /**
     * A goal that is solved when the agent manage to be in some distance close to a given block.
     * If the block is reachable from the agents current position, use the tactic navigateToBlockTAC.
     * If the block is not reachable, check for unexplored nodes, using the tactic exploreTAC.
     * If everything is explored and the block is still not reachable check if pressing buttons will help, using the tactic ____.
     * The goal is aborted if the destination is not reachable and all reachable unexplored nodes have been explored.
     */
    public static Function<UUSeAgentState,GoalStructure> smartCloseToBlock(TestAgent agent,
                                                                           String targetBlockName,
                                                                           Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                           SEBlockFunctions.BlockSides side,
                                                                           float delta) {

        String goalname_ = "close to block " + targetBlockName;
        String goalname2_ = "facing towards block " + targetBlockName;

        return (UUSeAgentState state) -> {
            Function<Void, GoalStructure> G = (unused) -> goal(goalname_)
                    .toSolve((Pair<Vec3,Vec3> posAndOrientation) -> {
                        WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state));
                        if (block == null) return false;

                        Vec3 targetLocation = SEBlockFunctions.getSideCenterPoint(block, side, delta + 1.5f);
                        Vec3 targetSquareCenter = state.getBlockCenter(targetLocation);

                        var agentPosition = posAndOrientation.fst ;
                        return Vec3.sub(targetSquareCenter, agentPosition).lengthSq() <= UUTacticLib.THRESHOLD_SQUARED_DISTANCE_TO_SQUARE ;
                    })
                    .withTactic(
                            FIRSTof(UUTacticLib.navigateToBlockTAC(selector, side, delta), ABORT()) )
                    .lift() ;

            return FIRSTof(
                    SEQ(G.apply(null), // try to go to the goal. if not possible, go explore
                        DEPLOYonce(agent, faceTowardBlock(goalname2_, selector))
                    ),
                    SEQ(explore(targetBlockName, selector, side, delta).apply(state),
                        G.apply(null), // now with new knowledge, try again
                        DEPLOYonce(agent, faceTowardBlock(goalname2_, selector))
                    ),
                    SEQ( // if still not possible, go to a button and press it
                        DEPLOYonce(agent, closeToButton(0)),
                        pressButton(0, 0), // pressing a button will set the label of the nodes of all known doors to unknown
                        explore(targetBlockName, selector, side, delta).apply(state), // now explore new possible locations (if any doors were reachable)
                        G.apply(null), // final check if we can go to the goal
                        DEPLOYonce(agent, faceTowardBlock(goalname2_, selector))
                    )
            );
        };
    }

    /**
     * A goal that is solved when the agent has no reachable unknown locations or the target block becomes reachable.
     * If an unknown location is reachable from the agents current position, use the tactic exploreTAC.
     * The goal is aborted if all reachable unexplored nodes have been explored.
     */
    public static Function<UUSeAgentState,GoalStructure> explore(String targetBlockName,
                                                                 Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                 SEBlockFunctions.BlockSides side,
                                                                 float delta) {
        return (UUSeAgentState state) -> {
            GoalStructure G = goal("exploring the world to find " + targetBlockName)
                    .toSolve((Boolean success) -> success)
                    .withTactic(
                            FIRSTof(UUTacticLib.exploreTAC(selector, side, delta), ABORT()) )
                    .lift();
            return G;
        };
    }

    /**
     * A goal that is solved when the agent has no reachable unknown locations.
     * If an unknown location is reachable from the agents current position, use the tactic exploreTAC.
     * The goal is aborted if all reachable unexplored nodes have been explored.
     */
    public static Function<UUSeAgentState,GoalStructure> explore() {
        return (UUSeAgentState state) -> {
//            return (GoalStructure) goal("exploring the world")
//                    .toSolve((Pair<Vec3,Vec3> posAndOrientation) -> {
//                        var agentSq = state.getGridPos(state.centerPos());
//                        List<DPos3> path = state.pathfinder.explore(state.getGrid(), agentSq);
//                        // the pathfinder cannot find a path to an unknown node, meaning the world is explored
//                        return path == null;
//                    })
//                    .withTactic(UUTacticLib.exploreTAC(), SUCCESS())
//                    .lift();
            return lift((UUSeAgentState state2) -> {
                var agentSq = state.getGridPos(state.centerPos());
                List<DPos3> path = state.pathfinder.explore(state.getGrid(), agentSq);
                // the pathfinder cannot find a path to an unknown node, meaning the world is explored
                return path == null;
            });
        };
    }

    public static Function<UUSeAgentState,GoalStructure> closeTo(Vec3 targetLocation) {
        return closeTo(null,targetLocation) ;
    }

    public static Function<UUSeAgentState,GoalStructure> exploreTo(Vec3 targetLocation) {
        return exploreTo(null,targetLocation) ;
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
     * 3D version of closeTo
     */
    public static Function<UUSeAgentState, GoalStructure> close3DTo(TestAgent agent,
                                                                    String blockType,
                                                                    SEBlockFunctions.BlockSides side,
                                                                    float radius,
                                                                    float delta) {
        float sqradius = radius * radius ;

        return close3DTo(agent,
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
     * smart version of closeTo3DTo, that assumes the goal block is reachable, but not necessarily right now.
     * It may have to explore a bit or press a button to open a door.
     */
    public static Function<UUSeAgentState, GoalStructure> smartClose3DTo(TestAgent agent,
                                                                         String blockType,
                                                                         SEBlockFunctions.BlockSides side,
                                                                         float radius,
                                                                         float delta) {
        float sqradius = radius * radius ;

        return smartCloseToBlock(agent,
                blockType,
                (UUSeAgentState state) -> (WorldEntity e)
                        ->
                        blockType.equals(e.getStringProperty("blockType"))
                                && Vec3.sub(e.position, state.wom.position).lengthSq() <= sqradius,
                side,
                delta
        ) ;
    }

    /**
     * A goal that is solved when the agent manage to get close (the distance is specified by delta) to the center of
     * the nearest block of the specified button-panel-type, within the given radius, and is facing towards the panel
     * using the specified face (front/back/ left/right) it is at.
     * The goal fails if there is no such block in the given radius, or if the agent cannot find a path
     * to the closest one.
     *
     * NOTE: for now the button-panel should be upright.
     */
    public static Function<UUSeAgentState,GoalStructure> closeToButton(TestAgent agent,
                                                                       String blockType,
                                                                       SEBlockFunctions.BlockSides side,
                                                                       float radius,
                                                                       float delta) {
        float sqradius = radius * radius ;

        return closeToButton(agent,
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
     * Use this to target a block in 2D using a generic selector function.
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
    /**
     * Use this to target a block in 3D using a generic selector function.
     */
    public static Function<UUSeAgentState, GoalStructure> close3DTo(TestAgent agent,
                                                                    String selectorDesc,
                                                                    Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                    SEBlockFunctions.BlockSides side,
                                                                    float delta) {


        return  (UUSeAgentState state) -> {

            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state)) ;
            if (block == null) return FAIL("Navigating autofail; no block can be found: " + selectorDesc);

            Vec3 intermediatePosition = SEBlockFunctions.getSideCenterPoint(block, side, delta + 1.5f);
            Vec3 goalPosition = SEBlockFunctions.getSideCenterPoint(block, side, delta);
            Vec3 blockCenter = (Vec3) block.getProperty("centerPosition");

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
//                    face2DTowardUpDown("facing towards a block of property " + selectorDesc + " @"
//                            + block.position
//                            + " ," + side, blockCenter)
            ) ;
        } ;
    }

    /**
     * Use this to target a button panel using a generic selector function.
     */
    public static Function<UUSeAgentState,GoalStructure> closeToButton(TestAgent agent,
                                                                       String selectorDesc,
                                                                       Function<UUSeAgentState, Predicate<WorldEntity>> selector,
                                                                       SEBlockFunctions.BlockSides side,
                                                                       float delta) {


        return  (UUSeAgentState state) -> {

            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom, selector.apply(state)) ;
            if (block == null)
                return FAIL("Navigating autofail; no block can be found: " + selectorDesc) ;
            if(!block.type.equals("block"))
                return FAIL("Navigating autofail; block not of type block: " + selectorDesc) ;
            if(!block.getStringProperty("blockType").contains("ButtonPanel"))
                return FAIL("Navigating autofail; block not of type ButtonPanel: " + selectorDesc) ;

            Vec3 blockCenter = (Vec3) block.getProperty("centerPosition") ;
            Vec3 goalPosition = blockCenter.copy() ;
            Vec3 intermediatePosition = blockCenter.copy() ;

            // move the center of button panels
            switch (side) {
                case FRONT  : intermediatePosition.x -=  delta ; break ;
                case BACK   : intermediatePosition.x +=  delta ; break ;
                case RIGHT  : intermediatePosition.z -=  delta ; break ;
                case LEFT   : intermediatePosition.z +=  delta ; break ;
                case TOP    : intermediatePosition.y -=  delta ; break ;
                case BOTTOM : intermediatePosition.y +=  delta ; break ;
            }

            switch (side) {
                case FRONT : blockCenter.x  -=  1 ; blockCenter.y  -=  0.5f ; break ;
                case BACK  : blockCenter.x  +=  1 ; blockCenter.y  -=  0.5f ; break ;
                case RIGHT : blockCenter.z  -=  1 ; blockCenter.y  -=  0.5f ; break ;
                case LEFT  : blockCenter.z  +=  1 ; blockCenter.y  -=  0.5f ; break ;
                //case TOP    : blockCenter.y +=  getActualSize(block).y * 0.5f + delta ; break ;
                //case BOTTOM : blockCenter.y -=  getActualSize(block).y * 0.5f + delta ; break ;
            }

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
                            + " ," + side, blockCenter),
                    face2DTowardUpDown("facing* towards a block of property " + selectorDesc + " @"
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

    public static GoalStructure face2DTowardUpDown(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "face towards " + p ;
        }
        return goal(goalname)
                .toSolve((Float cos_alpha) -> 1 - cos_alpha <= 0.01)
                .withTactic(FIRSTof(UUTacticLib.zTurnTowardACT(p).lift() , ABORT()))
                .lift() ;
    }

    public static GoalStructure face3DToward(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "face towards " + p ;
        }
        return goal(goalname)
                .toSolve((Float cos_alpha) -> 1 - cos_alpha <= 0.01)
                .withTactic(FIRSTof(UUTacticLib.yTurnTowardACT(p).lift() , ABORT()))
                .lift() ;
    }
}
