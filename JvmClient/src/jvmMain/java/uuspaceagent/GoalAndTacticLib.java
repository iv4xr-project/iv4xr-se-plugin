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
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Observation;
import spaceEngineers.model.ToolbarLocation;
import spaceEngineers.model.Vec2;


import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class GoalAndTacticLib {

    /**
     * If the angle between the agent's current direction and the direction-to-go is less
     * than this threshold, we slow the agent's turning speed, to mitigate that it might
     * overshoot the intended direction to go.
     * Expressed in terms of cos(angle). Below is cos(10-degree):
     */
    public static float THRESHOLD_ANGLE_TO_SLOW_TURNING = (float) Math.cos(Math.toRadians(10f));

    /**
     * When the agent's distance to a square's center is less or equal to this threshold, the
     * square is considered as visited. This distance is a about the square size, so the agent
     * might actually in the neighboring square. This threshold is introduced to avoid the
     * agent from getting stuck because it keeps overshooting the center of a target square.
     *
     * The threshold is not expressed literally as distance, but for efficiency it is expressed
     * as the square of the distance (so that we don't have to keep calculating square-roots).
     */
    public static float THRESHOLD_SQUARED_DISTANCE_TO_SQUARE = NavGrid.CUBE_SIZE * NavGrid.CUBE_SIZE; //1.3f * Grid2DNav.SQUARE_SIZE * 1.3f * Grid2DNav.SQUARE_SIZE

    public static float THRESHOLD_SQUARED_DISTANCE_TO_POINT= 1.7f ; // magic number ... :|

    public static float TURNING_SPEED = 10f ;

    /**
     * Walking speed (as defined by SE). Set as -0.3f. The negative is intentional and correct :)
     */
    public static float WALK_SPEED = -0.3f ;

    /***
     * At this speed the agent should run.
     */
    public static float RUN_SPEED = -0.4f ;

    static Vec3 FORWARDV3= new Vec3(0,0,1) ;
    static Vec3 ZEROV3 = new Vec3(0,0,0) ;
    static Vec2 ZEROV2 = new Vec2(0,0) ;

    /**
     * A more intelligent and performant primitive "move". This sends a burst of successive move
     * commands to SE with little computation in between. This results in fast(er) move. The number
     * of commands sent is specified by the parameter "duration", though the burst is stopped once
     * the agent is very close to the destination (some threshold). The agent will run, if the
     * distance to the destination is >= 1, else it will walk.
     *
     * Note that during the burst we will be blind to changes from SE.
     *
     * The method returns an observation if it did at least one move. If at the beginning the agent
     * is already (very close to) at the destination the method will not do any move and will
     * return null.
     */
    public static CharacterObservation moveTo(USeAgentState agentState, Vec3 destination, int duration) {
        Vec3 destinationRelativeLocation = Vec3.sub(destination,agentState.wom.position) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        // else, decide if we should run or walk:
        boolean running = true ;
        Vec3 forwardRun  = Vec3.mul(FORWARDV3,RUN_SPEED) ;
        Vec3 forwardWalk = Vec3.mul(FORWARDV3,WALK_SPEED) ;
        if( sqDistance <= 1) running = false ;
        // adjust the forward vector to make it angles towards the destination
        Matrix3D rotation = Matrix3D.getRotationXZ(destinationRelativeLocation, agentState.orientationForward()) ;
        forwardRun  = rotation.apply(forwardRun) ;
        forwardWalk = rotation.apply(forwardWalk) ;

        //if (!running || duration==null) {
        //    duration = 1 ;  // for walking, we will only maintain the move for one update
        //}
        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        float threshold = THRESHOLD_SQUARED_DISTANCE_TO_POINT - 0.15f ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(running ? forwardRun : forwardWalk)
                    ,ZEROV2,
                    0) ; // the last is "roll" ;
            sqDistance = Vec3.sub(SEBlockFunctions.fromSEVec3(obs.getPosition()),destination).lengthSq() ;
            if(sqDistance <= threshold) {
                break ;
            }
            if (running && sqDistance <= 1f) running = false ;
        }
        return obs ;
    }

    /**
     * A primitive method that sends a burst of successive turn-angle commands to SE. The number
     * of the commands is specified by the parameter duration. The agent will turn on its place
     * so that it would face the given destination. The parameter "cosAlphaThreshold" specifies how far
     * the agent should turn. It would turn until the angle alpha between its forward direction
     * and the straight line between itself and destination is alpha. The cosAlphaThreshold expresses
     * this alpha in terms of cos(alpha).
     *
     * The method will burst the turning, if the remaining angle towards alpha is still large,
     * afterwhich it will not burst (so it will just send one turn-command and return).
     *
     * If at the beginning the angle to destination is less than alpha this method returns null,
     * and else it returns an observation at the end of the turns.
     */
    public static CharacterObservation turnTo(USeAgentState agentState, Vec3 destination, float cosAlphaThreshold, Integer duration) {
        // direction vector to the next node:
        Vec3 dirToGo = Vec3.sub(destination,agentState.wom.position) ;
        Vec3 agentHdir = agentState.orientationForward() ;
        // for calculating 2D rotation we ignore the y-value:
        dirToGo.y = 0 ;
        agentHdir.y = 0 ;
        dirToGo = dirToGo.normalized() ;
        agentHdir = agentHdir.normalized() ;
        // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
        var cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
        //if(1 - cos_alpha < 0.01) {
        if(cos_alpha > cosAlphaThreshold) {
            // the angle is already quite aligned to the direction of where we have to go, no turning.
            return null ;
        }

        float turningSpeed = TURNING_SPEED ;
        boolean fastturning = true ;

        Vec3 normalVector = Vec3.cross(agentHdir,dirToGo) ;

        if(cos_alpha >= THRESHOLD_ANGLE_TO_SLOW_TURNING) {
            // the angle between the agent's own direction and target direction is less than 10-degree
            // we reduce the turning-speed:
            turningSpeed = TURNING_SPEED*0.25f ;
            fastturning = false ;
        }
        // check if we have to turn clockwise or counter-clockwise
        if (normalVector.y > 0) {
            // the dir-to-go is to the "left"/counter-clockwise direction
            turningSpeed = - turningSpeed ;
        }
        if(!fastturning || duration == null) {
            duration = 1 ;
        }
        Vec2 turningVector = new Vec2(0, turningSpeed) ;

        // now send the turning commands:
        CharacterObservation obs = null ;
        for (int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(ZEROV3),
                    turningVector,
                    0) ; // the last is "roll"
        }

        return obs ;
    }

    /**
     * A primitive method that will send successive grind-commends to SE. The number of commands
     * is specified by the parameter "duration". The method does not check if in between
     * the targeted block is actually already destroyed.
     */
    public static void grind(USeAgentState state, int duration) {
        for(int k=0; k<duration; k++) {
            state.env().beginUsingTool();
        }
    }

    /**
     * A goal that is solved when the agent manage to be in some distance close to a
     * given destination. The destination itself should be reachable from the agent
     * current position. The solver for this goal is the tactic navigate2DToTAC.
     *
     * The goal is aborted if the destination is not reachable.
     */
    public static GoalStructure close2Dto(String goalname, Vec3 targetLocation) {
        Vec3[] targetSquareCenter_ = {null} ; // a state to memoize some calculations
        if(goalname == null) {
            goalname = "close to location " + targetLocation ;
        }
        GoalStructure G = goal(goalname)
                .toSolve((USeAgentState state) -> {
                    if(targetSquareCenter_[0] == null) {
                        targetSquareCenter_[0] = state.navgrid.getSquareCenterLocation(state.navgrid.gridProjectedLocation(targetLocation));
                    }
                    var targetSquareCenter = targetSquareCenter_[0] ;
                    return Vec3.sub(targetSquareCenter,state.wom.position).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE ;
                })
                .withTactic(
                        FIRSTof(navigate2DToTAC(targetLocation), ABORT())
                )
                .lift() ;
        return G ;
    }

    public static GoalStructure close2Dto(Vec3 targetLocation) {
        return close2Dto(null,targetLocation) ;
    }

    /**
     * A goal that is solved when the agent manage to get close (the distance is specified by delta) to the center of a
     * specified face (front/back/ left/right) of the nearest block of the specified block-type, within the given
     * radius. The goal fails if there is no such block in the given radius, or if the agent cannot find a path
     * to the closest one.
     *
     * NOTE: for now the block should be a cube, and upright.
     */
    public static GoalStructure close2Dto(TestAgent agent,
                                          String blockType,
                                          SEBlockFunctions.BlockSides side,
                                          float radius,
                                          float delta) {
        float sqradius = radius * radius ;

        return close2Dto(agent,
                "type " + blockType,
                (USeAgentState state) -> (WorldEntity e)
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
    public static GoalStructure close2Dto(TestAgent agent,
                                          String selectorDesc,
                                          Function<USeAgentState, Predicate<WorldEntity>> selector,
                                          SEBlockFunctions.BlockSides side,
                                          float delta) {


        GoalStructure G = DEPLOYonce(agent, (USeAgentState state) -> {

            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom,selector.apply(state)) ;
            if (block == null) return FAIL("Navigating autofail; no block can be found: " + selectorDesc) ;

            Vec3 intermediatePosition = SEBlockFunctions.getSideCenterPoint(block,side,delta + 1.5f) ;
            Vec3 goalPosition = SEBlockFunctions.getSideCenterPoint(block,side,delta) ;
            Vec3 blockCenter = (Vec3) block.getProperty("centerPosition") ;

            return SEQ(close2Dto("close to a block of property " + selectorDesc + " @"
                            + block.position
                            + " ," + side + ", targeting " + intermediatePosition,
                            intermediatePosition),
                      veryclose2DTo("very close to a block of property " + selectorDesc + " @"
                              + block.position
                              + " ," + side + ", targeting " + goalPosition,
                              goalPosition),
                      face2DToward("facing towards a block of property " + selectorDesc + " @"
                              + block.position
                              + " ," + side, blockCenter)
                    ) ;
        }) ;

        return G ;
    }

    public static GoalStructure grinderEquiped() {
        return lift("Grinder equiped",
                  action("equip grinder").do1((USeAgentState state) -> {
                     state.env().equip(new ToolbarLocation(0,0));
                     return true ;
                  })
                ) ;
    }

    public static GoalStructure barehandEquiped() {
        return lift("Grinder equiped",
                action("equip grinder").do1((USeAgentState state) -> {
                    state.env().equip(new ToolbarLocation(0,9));
                    return true ;
                })
        ) ;
    }

    public static GoalStructure photo(String fname) {
        return lift("Screenshot made",
                action("Snapping a picture").do1((USeAgentState state) -> {
                    state.env().getController().getObserver().takeScreenshot(fname);
                    return true ;
                })
        ) ;
    }


    public static GoalStructure targetBlockOK(TestAgent agent, Predicate<WorldEntity> predicate, boolean abortIfFail) {

        Tactic checkAction = action("checking a predicate")
                .do1(state -> true)
                .on_((USeAgentState state) -> {
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

        Tactic success = action("success").do1((USeAgentState state) -> true).lift() ;

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

        GoalStructure grind = DEPLOYonce(agent, (USeAgentState state) -> {
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
                                .do1((USeAgentState st) -> {
                                    grind(state,50);
                                    Observation rawGridsAndBlocksStates = st.env().getController().getObserver().observeBlocks() ;
                                    WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates) ;
                                    return SEBlockFunctions.findWorldEntity(st.wom,targetId) ;
                                })
                        .lift())
                    .lift() ;
        }) ;

        GoalStructure stopGrinding = lift("Grinding stopped",
                action("stop grinding")
                     .do1((USeAgentState st) -> {
                         st.env().endUsingTool();
                         return true ;
                      })
                ) ;

        return SEQ(grinderEquiped(), grind, stopGrinding, barehandEquiped()) ;
    }

    /**
     * A tactic; if executed repeatedly it guides the agent to get to some distance close to a
     * given destination, if it is reachable. 2D grid-based pathfinding is used to do this.
     * To be more precise, the tactic targets the square S on the grid where the destination is
     * located. Then it travels to some point with the distance at most d to the center of S.
     * D is the square root of THRESHOLD_SQUARED_DISTANCE_TO_SQUARE.
     */
    public static Tactic navigate2DToTAC(Vec3 destination) {

        return action("navigateTo").do2((USeAgentState state)
                        -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst ;
                    var arrivedAtDestination = queryResult.snd ;
                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        return state ;
                    }
                    // else we are not at the destination yet...

                    // set currentPathToFollow:
                    state.currentPathToFollow = path ;

                    // follow the path, direct the agent to the next node in the path (actually, the first
                    // node in the path, since we remove a node if it is passed):
                    var nextNode = state.currentPathToFollow.get(0) ;
                    var nextNodePos = state.navgrid.getSquareCenterLocation(nextNode) ;
                    var agentSq = state.navgrid.gridProjectedLocation(state.wom.position) ;
                    //if(agentSq.equals(nextNode)) {
                    if(Vec3.sub(nextNodePos,state.wom.position).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        return state ;
                    }
                    var obs = turnTo(state,nextNodePos, 0.75f,10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        return state ;
                    }
                    moveTo(state,nextNodePos,20) ;
                    return state ;
                } )
                .on((USeAgentState state)  -> {
                    if (state.wom==null) return null ;
                    //var agentPos = state.wom.position ;
                    var agentSq = state.navgrid.gridProjectedLocation(state.wom.position) ;
                    var destinationSq = state.navgrid.gridProjectedLocation(destination) ;
                    var destinationSqCenterPos = state.navgrid.getSquareCenterLocation(destinationSq) ;
                    //if (state.grid2D.squareDistanceToSquare(agentPos,destinationSq) <= SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION) {
                    //if(agentSq.equals(destinationSq)) {
                    if(Vec3.sub(destinationSqCenterPos,state.wom.position).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {

                            // the agent is already at the destination. Just return the path, and indicate that
                        // we have arrived at the destination:
                        return new Pair<>(state.currentPathToFollow,true) ;
                    }
                    int currentPathLength = state.currentPathToFollow.size() ;
                    if (currentPathLength == 0
                            || ! destinationSq.equals(state.currentPathToFollow.get(currentPathLength - 1)))
                    {  // there is no path planned, or there is an ongoing path, but it goes to a different target
                        List<DPos3> path = state.pathfinder2D.findPath(state.navgrid, agentSq, destinationSq)  ;
                        if (path == null) {
                            // the pathfinder cannot find a path. The tactic is then not enabled:
                            return null ;
                        }
                        path = smoothenPath(path) ;
                        return new Pair<>(path,false) ;
                    }
                    else {
                        // the currently followed path leads to the specified destination, and  furthermore we are not
                        // at the destination yet:
                        return new Pair<>(state.currentPathToFollow,false) ;
                    }
                })
                .lift();
    }

    /**
     * Optimize a path such that every segment v1,v2,v3 that lie on the same line, then we remove
     * v2 from the path.
     */
    public static List<DPos3> smoothenPath(List<DPos3> path) {
        if(path.size() <= 2) return path ;
        int k = 0 ;
        while(k < path.size() - 2) {
            var v1 = path.get(k).toVec3() ;
            var v2 = path.get(k+1).toVec3() ;
            var v3 = path.get(k+2).toVec3() ;

            var gradient_v1v2 = Vec3.sub(v2,v1).normalized() ;
            var gradient_v2v3 = Vec3.sub(v3,v2).normalized() ;
            float cos_alpha = Vec3.dot(gradient_v1v2, gradient_v2v3) ;
            if (cos_alpha >= 0.99) {
                // the gradients v1-->v2 and v2-->v3 are the same or almost the same,
                // so v1--v2--v3 are on the same line, or almost at the same line.
                // We then remove v2:
                path.remove(k+1) ;
            }
            else {
                k++ ;
            }
        }
        return path ;
    }


    public static GoalStructure veryclose2DTo(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "very close to " + p ;
        }
        return goal(goalname)
                .toSolve((Float square_distance) -> {
                    //System.out.println(">> sq-dist " + square_distance) ;
                    return square_distance <= THRESHOLD_SQUARED_DISTANCE_TO_POINT ;
                })
                .withTactic(FIRSTof(straightlineMove2DTowards(p).lift() , ABORT()))
                .lift() ;
    }

    /**
     * When invoked repeatedly, this action drives the agent to move in the straight-line towards the given
     * destination. The destination is assumed to be on the same XZ plane as the agent. The space between
     * the agent and the destination is assumed to be clear, and the XZ-plane along the travel is walkable.
     *
     * The action is no longer enabled if the agent is already at the destination (or very very close to it).
     *
     * This action will not turn the agent to face the destination (so, it will strafe, if its orientation
     * is not aligned first).
     */
    public static Action straightlineMove2DTowards(Vec3 destination) {

        return action("straight line move to " + destination)
                .do1((USeAgentState state) -> {
                    var sqDistance = Vec3.sub(destination,state.wom.position).lengthSq() ;
                    moveTo(state,destination,7) ;
                    return sqDistance ;
                })
                .on_((USeAgentState state) -> Vec3.sub(destination, state.wom.position).lengthSq() >= 0.01)
                ;
    }

    public static GoalStructure face2DToward(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "face towards " + p ;
        }
        return goal(goalname)
                .toSolve((Float cos_alpha) -> 1 - cos_alpha <= 0.01)
                .withTactic(FIRSTof(rotateXY(p).lift() , ABORT()))
                .lift() ;
    }

    /**
     * When invoked repeatedly, this action turns the agent until it horizontally faces towards the
     * given destination. The turning is on the XZ plane (so, the y coordinates on all poins in the
     * agent would stays the same). When the agent faces towards the destination (with some epsilon),
     * the action is no longer enabled.
     */
    public static Action rotateXY(Vec3 destination) {

        float cosAlphaThreshold  = 0.99f ;
        float cosAlphaThreshold_ = 0.995f ;

        return action("turning towards " + destination)
                .on((USeAgentState state) ->{
                    Vec3 dirToGo = Vec3.sub(destination,state.wom.position) ;
                    Vec3 forwardOrientation = state.orientationForward() ;
                    dirToGo.y = 0 ;
                    forwardOrientation.y = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    var cos_alpha = Vec3.dot(forwardOrientation,dirToGo) ;
                    if(cos_alpha >= cosAlphaThreshold) { // the angle is quite aligned, the action is disabled
                        return null ;
                    }
                    return cos_alpha ;
                })
                .do2((USeAgentState state) -> (Float cos_alpha) -> {
                    CharacterObservation obs = turnTo(state,destination,cosAlphaThreshold_,10) ;
                    if(obs == null) {
                        return cos_alpha ;
                    }
                    Vec3 dirToGo = Vec3.sub(destination,state.wom.position) ;
                    Vec3 forwardOrientation = SEBlockFunctions.fromSEVec3(obs.getOrientationForward()) ;
                    dirToGo.y = 0 ;
                    forwardOrientation.y = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    cos_alpha = Vec3.dot(forwardOrientation,dirToGo) ;
                    return cos_alpha ;
                }) ;
    }

}
