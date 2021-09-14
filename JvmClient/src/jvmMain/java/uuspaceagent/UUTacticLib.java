package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Action;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Vec2;

import java.util.List;

import static nl.uu.cs.aplib.AplibEDSL.action;

public class UUTacticLib {

    /**
     * The default speed with which the agent turns.
     */
    public static float TURNING_SPEED = 10f ;

    /**
     * The default walking speed. Set as 0.3f, as defined by SE (at this speed the agent
     * will walk, not running).
     */
    public static float WALK_SPEED = 0.3f ;

    /***
     * The default running speed. This is set at 0.4. SE defines that at this speed the
     * agent will run.
     */
    public static float RUN_SPEED = 0.4f ;

    /**
     * The unit move-vector that will cause the agent to move in the same direction as its
     * forward-orientation. That is, this vector will move the agent in the direction that
     * it faces to. Note that as a unit-vector it does not take speed into account; you need
     * to multiply it with the speed that you want.
     */
    static Vec3 FORWARDV3= new Vec3(0,0,1) ;

    /**
     * Just the vector (0,0,0).
     */
    static Vec3 ZEROV3 = new Vec3(0,0,0) ;

    /**
     * Just the vector (0,0).
     */
    static Vec2 ZEROV2 = new Vec2(0,0) ;

    /**
     * A more intelligent and performant primitive "move" (than the primitive "moveAndRotate"). This
     * method sends a burst of successive move commands to SE with little computation in between.
     * This results in fast(er) move. The number
     * of commands sent is specified by the parameter "duration", though the burst is stopped once
     * the agent is very close to the destination (some threshold). The agent will run, if the
     * distance to the destination is >= 1, else it will walk.
     *
     * Note: (1) the method will not turn the direction the agent is facing,
     * and (2) that during the burst we will be blind to changes from SE.
     *
     * The method returns an observation if it did at least one move. If at the beginning the agent
     * is already (very close to) at the destination the method will not do any move and will
     * return null.
     */
    public static CharacterObservation moveToward(UUSeAgentState agentState, Vec3 destination, int duration) {
        Vec3 destinationRelativeLocation = Vec3.sub(destination,agentState.wom.position) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        System.out.println(">>> agent @ " + agentState.wom.position + ", dest: " + destination
           + ", rel-direction: " + destinationRelativeLocation);
        System.out.println("    forward-vector: " + agentState.orientationForward());
        // else, decide if we should run or walk:
        boolean running = true ;
        Vec3 forwardRun  = Vec3.mul(FORWARDV3, RUN_SPEED) ;
        Vec3 forwardWalk = Vec3.mul(FORWARDV3, WALK_SPEED) ;
        if( sqDistance <= 1) running = false ;
        // adjust the forward vector to make it angles towards the destination
        if(! agentState.jetpackRunning()) {
            // 2D movement on surface:
            Matrix3D rotation = Rotation.getYRotation(agentState.orientationForward(),destinationRelativeLocation) ;
            forwardRun  = rotation.apply(forwardRun) ;
            forwardWalk = rotation.apply(forwardWalk) ;
            System.out.println(">>> no-fly forwardRun: " + forwardRun);
        }
        else {
            forwardRun = Rotation.rotate(forwardRun, agentState.orientationForward(), destinationRelativeLocation) ;
            forwardWalk = Rotation.rotate(forwardWalk, agentState.orientationForward(), destinationRelativeLocation) ;
            // applly correction on the y-component, taking advantage that we know
            // the agent's forward orientation has its y-component 0.
            forwardRun.y = Math.abs(forwardRun.y) ;
            forwardWalk.y = Math.abs(forwardWalk.y) ;
            if (destinationRelativeLocation.y < 0) {
                forwardRun.y = - forwardRun.y ;
                forwardWalk.y = - forwardWalk.y ;
            }
            System.out.println(">>> FLY forwardRun: " + forwardRun);
        }

        //if (!running || duration==null) {
        //    duration = 1 ;  // for walking, we will only maintain the move for one update
        //}
        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        float threshold = UUGoalLib.THRESHOLD_SQUARED_DISTANCE_TO_POINT - 0.15f ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(running ? seFixPolarityMoveVector(forwardRun) : seFixPolarityMoveVector(forwardWalk))
                    , ZEROV2,
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
     * A primitive method that sends a burst of successive turn-angle commands to SE. This
     * method will only change the agent's forward facing vector. So, it rotates the agent's
     * forward vector around the agent's y-axis. It does not change the agent's up-facing
     * vector either. (so, it is like moving the agent's head horizontally, but not vertically).
     *
     * The number of the turn-commands bursted is specified by the parameter duration. The agent
     * will turn on its place so that it would face the given destination. The parameter
     * "cosAlphaThreshold" specifies how far the agent should turn. It would turn until the angle
     * alpha between its forward direction and the straight line between itself and destination
     * is alpha. The cosAlphaThreshold expresses this alpha in terms of cos(alpha).
     *
     * The method will burst the turning, if the remaining angle towards alpha is still large,
     * after which it will not burst (so it will just send one turn-command and return).
     *
     * If at the beginning the angle to destination is less than alpha this method returns null,
     * and else it returns an observation at the end of the turns.
     */
    public static CharacterObservation yTurnTowardACT(UUSeAgentState agentState, Vec3 destination, float cosAlphaThreshold, Integer duration) {
        // direction vector to the next node:
        Vec3 dirToGo = Vec3.sub(destination,agentState.wom.position) ;
        Vec3 agentHdir = agentState.orientationForward() ;
        // for calculating 2D rotation we ignore the y-value:
        dirToGo.y = 0 ;
        agentHdir.y = 0 ;

        if(dirToGo.lengthSq() < 1) {
            // the destination is too close within the agent's y-cylinder;
            // don't bother to rotate then
            return  null ;
        }

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

        if(cos_alpha >= UUGoalLib.THRESHOLD_ANGLE_TO_SLOW_TURNING) {
            // the angle between the agent's own direction and target direction is less than 10-degree
            // we reduce the turning-speed:
            turningSpeed = TURNING_SPEED*0.25f ;
            fastturning = false ;
        }
        // check if we have to turn clockwise or counter-clockwise
        if (normalVector.y > 0) {
            // The agent should then turn clock-wise; for SE this means giving
            // a negative turning speed. Else positive turning speed.
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
            dirToGo = Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getPosition())) ;
            agentHdir = SEBlockFunctions.fromSEVec3(obs.getOrientationForward()) ;
            // for calculating 2D rotation we ignore the y-value:
            dirToGo.y = 0 ;
            agentHdir.y = 0 ;

            if(dirToGo.lengthSq() < 1) {
                // the destination is too close within the agent's y-cylinder;
                // don't bother to rotate then
                break ;
            }

            dirToGo = dirToGo.normalized() ;
            agentHdir = agentHdir.normalized() ;
            // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
            cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
            //if(1 - cos_alpha < 0.01) {
            if(cos_alpha > cosAlphaThreshold) {
                // the angle is already quite aligned to the direction of where we have to go, no turning.
               break ;
            }
        }

        return obs ;
    }

    /**
     * Fix the polarity of move-vector to be given to the SE's method moveAndRotate(). It
     * requires us to reverse the polarity of the values for x and z-axis, but not the y-axis.
     */
    public static Vec3 seFixPolarityMoveVector(Vec3 v) {
        return new Vec3(- v.x,  v.y, - v.z) ;
    }

    /**
     * A primitive method that will send successive grind-commends to SE. The number of commands
     * is specified by the parameter "duration". The method does not check if in between
     * the targeted block is actually already destroyed.
     */
    public static void grind(UUSeAgentState state, int duration) {
        for(int k=0; k<duration; k++) {
            state.env().beginUsingTool();
        }
    }

    /**
     * When invoked repeatedly, this action turns the agent until it horizontally faces towards the
     * given destination. The turning is around the y-axis (so, on the XZ plane; the y coordinates on all
     * points in the agent would stay the same). When the agent faces towards the destination
     * (with some epsilon), the action is no longer enabled.
     *
     * The action returns the resulting angle (expressed in cos-alpha) between the agent's
     * forward-orientation and the direction-vector towards the given destination.
     */
    public static Action yTurnTowardACT(Vec3 destination) {

        float cosAlphaThreshold  = 0.99f ;
        float cosAlphaThreshold_ = 0.995f ;

        return action("turning towards " + destination)
                .on((UUSeAgentState state) ->{
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
                .do2((UUSeAgentState state) -> (Float cos_alpha) -> {
                    CharacterObservation obs = yTurnTowardACT(state,destination,cosAlphaThreshold_,10) ;
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

    /**
     * When invoked repeatedly, this action drives the agent to move in the straight-line towards the given
     * destination. The destination is assumed to be on the same XZ plane as the agent. The space between
     * the agent and the destination is assumed to be clear, and the XZ-plane along the travel is walkable.
     *
     * The action is no longer enabled if the agent is already at the destination (or very very close to it).
     *
     * This action will not turn the agent to face the destination (so, it will strafe, if its orientation
     * is not aligned first).
     *
     * The action returns the resulting distance to the destination. More precisely, it returns
     * the square of the distance, to avoid calculating the more expensive square-root.
     */
    public static Action straightline2DMoveTowardsACT(Vec3 destination) {

        return action("straight line move to " + destination)
                .do1((UUSeAgentState state) -> {
                    var sqDistance = Vec3.sub(destination,state.wom.position).lengthSq() ;
                    CharacterObservation obs = moveToward(state,destination,7) ;
                    if(obs ==null) {
                        return sqDistance ;
                    }
                    return Vec3.sub(destination,SEBlockFunctions.fromSEVec3(obs.getPosition())).lengthSq() ;

                })
                .on_((UUSeAgentState state) -> Vec3.sub(destination, state.wom.position).lengthSq() >= 0.01)
                ;
    }

    /**
     * A tactic; if executed repeatedly it guides the agent to get to some distance close to a
     * given destination, if it is reachable. 2D grid-based pathfinding is used to do this.
     * To be more precise, the tactic targets the square S on the grid where the destination is
     * located. Then it travels to some point with the distance at most d to the center of S.
     * D is the square root of THRESHOLD_SQUARED_DISTANCE_TO_SQUARE.
     *
     * The tactic returns the resulting new position and forward-orientation of the agent.
     */
    public static Tactic navigateToTAC(Vec3 destination) {

        return action("navigateTo").do2((UUSeAgentState state)
                        -> (Pair<List<DPos3>, Boolean> queryResult) -> {
                    var path = queryResult.fst ;
                    var arrivedAtDestination = queryResult.snd ;

                    // check first if we should turn on/off jetpack:
                    if(state.wom.position.y - state.navgrid.origin.y <= NavGrid.AGENT_HEIGHT
                       && path.get(0).y == 0 && state.jetpackRunning()
                    ) {
                        state.env().getController().getCharacter().turnOffJetpack() ;
                    }
                    else {
                        if (path.get(0).y > 0 && !state.jetpackRunning()) {
                            state.env().getController().getCharacter().turnOnJetpack() ;
                        }
                    }

                    if (arrivedAtDestination) {
                        state.currentPathToFollow.clear();
                        return new Pair<>(state.wom.position, state.orientationForward()) ;
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
                    if(Vec3.sub(nextNodePos,state.wom.position).lengthSq() <= UUGoalLib.THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        return new Pair<>(state.wom.position, state.orientationForward()) ;
                    }
                    CharacterObservation obs = null ;
                    // disabling rotation for now
                    obs = yTurnTowardACT(state,nextNodePos, 0.8f,10) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward())) ;
                    }
                    obs = moveToward(state,nextNodePos,20) ;
                    return new Pair<>(SEBlockFunctions.fromSEVec3(obs.getPosition()), SEBlockFunctions.fromSEVec3(obs.getOrientationForward()))  ;
                } )
                .on((UUSeAgentState state)  -> {
                    if (state.wom==null) return null ;
                    //var agentPos = state.wom.position ;
                    var agentSq = state.navgrid.gridProjectedLocation(state.wom.position) ;
                    var destinationSq = state.navgrid.gridProjectedLocation(destination) ;
                    var destinationSqCenterPos = state.navgrid.getSquareCenterLocation(destinationSq) ;
                    //if (state.grid2D.squareDistanceToSquare(agentPos,destinationSq) <= SQEPSILON_TO_NODE_IN_2D_PATH_NAVIGATION) {
                    //if(agentSq.equals(destinationSq)) {
                    if(Vec3.sub(destinationSqCenterPos,state.wom.position).lengthSq() <= UUGoalLib.THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {

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
                        System.out.println("### PATH: " + PrintInfos.showPath(state,path));
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
}
