package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.*;
import static nl.uu.cs.aplib.AplibEDSL.* ;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Vec2;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nl.uu.cs.aplib.AplibEDSL.* ;

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
    public static float THRESHOLD_SQUARED_DISTANCE_TO_SQUARE = Grid2DNav.SQUARE_SIZE * Grid2DNav.SQUARE_SIZE ; //1.3f * Grid2DNav.SQUARE_SIZE * 1.3f * Grid2DNav.SQUARE_SIZE

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
     * A more intelligent and performant primitive "move". This will run for several frame updates
     * if the destination is still far ( >= 1), else it switches to walking. When running, to make
     * the agent run it will send a "move" command to SE in a rapid successions. The parameter
     * "duration" specifies how many moves this method will send. Because between the command the
     * Java side does no computation, this means that the whole series can be executed more rapidly
     * by SE, producing faster moves. However, throughout this duration, we will be blind to changes
     * from SE.
     *
     * The method returns an observation if it did at least one move. If the agent is already
     * (very close to) at the destination the method will not do any move and will return null.
     */
    public static CharacterObservation moveTo(USeAgentState agentState, Vec3 destination, Integer duration) {
        Vec3 destinationRelativeLocation = Vec3.sub(destination,agentState.wom.position) ;
        float sqDistance = destinationRelativeLocation.lengthSq() ;
        if (sqDistance <= 0.01) {
            // already very close to the destination
            return null ;
        }
        // else, decide if we should run or walk:
        boolean running = false ;
        Vec3 forwardSpeed = FORWARDV3 ;
        if( sqDistance <= 1) {
            forwardSpeed = Vec3.mul(forwardSpeed,WALK_SPEED) ;
        }
        else {
            running = true ;
            forwardSpeed = Vec3.mul(forwardSpeed,RUN_SPEED) ;
        }
        // adjust the forward vector to make it angles towards the destination
        Matrix3D rotation = Matrix3D.getRotationXZ(destinationRelativeLocation, agentState.orientationForward()) ;
        forwardSpeed = rotation.apply(forwardSpeed) ;

        if (!running || duration==null) {
            duration = 1 ;  // for walking, we will only maintain the move for one update
        }
        // now move... sustain it for the given duration:
        CharacterObservation obs = null ;
        for(int k=0; k<duration; k++) {
            obs = agentState.env().getController().getCharacter().moveAndRotate(
                    SEBlockFunctions.toSEVec3(forwardSpeed)
                    ,ZEROV2,
                    0) ; // the last is "roll"
        }
        return obs ;
    }

    public static CharacterObservation turnTo(USeAgentState agentState, Vec3 destination, Integer duration) {
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
        if(1 - cos_alpha < 0.01) {
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
                        targetSquareCenter_[0] = state.grid2D.getSquareCenterLocation(state.grid2D.gridProjectedLocation(targetLocation));
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

    public static GoalStructure FAIL(String gname) {
        return goal(gname)
            .toSolve((USeAgentState st) -> false)
            .withTactic(ABORT())
            .lift() ; }

    /**
     * The combinator will "dynamically" deploy a goal to be executed/adopted after executing this
     * combinator. The paramerter dynamic goal takes the agent current state and constructs a goal H
     * based on it, and this H is the one that is deployed.
     */
    public static <AgentState> GoalStructure  DEPLOYonce(TestAgent agent, Function<AgentState,GoalStructure> dynamicgoal) {
        Boolean[] deployed = {false} ;
        GoalStructure G = goal("deploy once")
                .toSolve((AgentState state) -> false )
                .withTactic(FIRSTof(
                        action("deploying a goal")
                            .do1((AgentState state) -> {
                                agent.addAfter(dynamicgoal.apply(state));
                                deployed[0] = true ;
                                //System.out.println(">>> action: deployed[0] = " + deployed[0]) ;
                                return state ;
                                })
                            .on_(state -> ! deployed[0] )
                            .lift(),
                        ABORT())
                ).lift();
        return FIRSTof(G) ;
    }

    public static GoalStructure close2Dto(TestAgent agent,
                                          String blockType,
                                          SEBlockFunctions.BlockSides side,
                                          float radius,
                                          float delta) {

        float sqradius = radius * radius ;

        GoalStructure G = DEPLOYonce(agent, (USeAgentState state) -> {

            WorldEntity block = SEBlockFunctions.findClosestBlock(state.wom,blockType,radius) ;
            if (block == null) return FAIL("Autofail: no " + blockType + " can be found!") ;

            Vec3 intermediatePosition = SEBlockFunctions.getSideCenterPoint(block,side,delta + 1.5f) ;
            Vec3 goalPosition = SEBlockFunctions.getSideCenterPoint(block,side,delta) ;
            Vec3 blockCenter = (Vec3) block.getProperty("centerPosition") ;

            return SEQ(close2Dto("close to the " + blockType + " @"
                            + block.position
                            + " ," + side + ", targeting " + intermediatePosition,
                            intermediatePosition),
                      veryclose2DTo("very close to the " + blockType + " @"
                              + block.position
                              + " ," + side + ", targeting " + goalPosition,
                              goalPosition),
                      face2DToward("facing towards " + blockType + " @"
                              + block.position
                              + " ," + side, blockCenter)
                    ) ;
        }) ;

        return G ;
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
                        -> (Pair<List<Pair<Integer,Integer>>, Boolean> queryResult) -> {
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
                    var nextNodePos = state.grid2D.getSquareCenterLocation(nextNode) ;
                    var agentSq = state.grid2D.gridProjectedLocation(state.wom.position) ;
                    //if(agentSq.equals(nextNode)) {
                    if(Vec3.sub(nextNodePos,state.wom.position).lengthSq() <= THRESHOLD_SQUARED_DISTANCE_TO_SQUARE) {
                        // agent is already in the same square as the next-node destination-square. Mark the node
                        // as reached (so, we remove it from the plan):
                        state.currentPathToFollow.remove(0) ;
                        return state ;
                    }
                    var obs = turnTo(state,nextNodePos,7) ;
                    if (obs != null) {
                        // we did turning, we won't move.
                        return state ;
                    }
                    /*
                    // direction vector to the next node:
                    Vec3 dirToGo = Vec3.sub(nextNodePos,state.wom.position) ;
                    Vec3 agentHdir = state.orientationForward() ;
                    // for calculating 2D rotation we ignore the y-value:
                    dirToGo.y = 0 ;
                    agentHdir.y = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    agentHdir = agentHdir.normalized() ;
                    // angle between the dir-to-go and the agent's own direction (expressed as cos(angle)):
                    var cos_alpha = Vec3.dot(agentHdir,dirToGo) ;
                    Vec3 normalVector = Vec3.cross(agentHdir,dirToGo) ;

                    float turningSpeed = TURNING_SPEED ;
                    var forward_speed = WALK_SPEED ;
                    if(1 - cos_alpha < 0.01) { // the angle is quite aligned to where we have to go, no turning
                        turningSpeed = 0 ;
                        // slow down if the target is closer:
                        var distsq = Vec3.sub(nextNodePos,state.wom.position).lengthSq() ;
                        if(distsq <= 1f) {
                            forward_speed = forward_speed * 0.33f ;
                        }
                    }
                    else {
                        // else, for now we will turn without moving. :|
                        forward_speed = 0 ;
                        if(cos_alpha >= THRESHOLD_ANGLE_TO_SLOW_TURNING) {
                            // the angle between the agent's own direction and target direction is less than 10-degree
                            // we reduce the turning-speed:
                            turningSpeed = TURNING_SPEED*0.25f ;
                        }
                        // check if we have to turn clockwise or counter-clockwise
                        if (normalVector.y > 0) {
                            // the dir-to-go is to the "left"/counter-clockwise direction
                            turningSpeed = - turningSpeed ;
                        }
                    }

                     */
                    /*
                    System.out.println("xxxx target: " + nextNode + ": " + nextNodePos
                            + ", agent @" + agentSq + ":"+ state.wom.position) ;
                    System.out.println("==== dir-to-go: " + dirToGo) ;
                    System.out.println("==== hdir : " + agentHdir) ;

                    System.out.println("==== cpsalpha: " + cos_alpha + ", alpha: " + Math.toDegrees(Math.acos(cos_alpha))) ;
                    System.out.println("==== normal.y: " + normalVector.y) ;
                    System.out.println("==== turning speed: " + turningSpeed) ;
                    System.out.println("==== forward speed: " + forward_speed) ;
                    */

            /*
                    Vec2 turningVector = new Vec2(0, turningSpeed) ;
                    spaceEngineers.model.Vec3 forwardVector = new spaceEngineers.model.Vec3(0,0,1 * forward_speed) ;

                    // Now send a command to move the agent:
                    //if(cos_alpha<=0) forward_speed = new spaceEngineers.model.Vec3(0,0,0) ;
                    if (turningSpeed != 0) {
                        state.env().getController().getCharacter().moveAndRotate(forwardVector , turningVector, 0) ; // the last is "roll"
                    }
                    else {
                        moveTo(state,nextNodePos,7) ;
                    }
                    */

                    moveTo(state,nextNodePos,7) ;

                    // moving will take some time. We will now return the state at this moment.
                    // The state will be sampled again after some d
                    //
                    //
                    //
                    //
                    // elta time, in the next agent-update.
                    return state ;
                } )
                .on((USeAgentState state)  -> {
                    if (state.wom==null) return null ;
                    //var agentPos = state.wom.position ;
                    var agentSq = state.grid2D.gridProjectedLocation(state.wom.position) ;
                    var destinationSq = state.grid2D.gridProjectedLocation(destination) ;
                    var destinationSqCenterPos = state.grid2D.getSquareCenterLocation(destinationSq) ;
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
                        List<Pair<Integer,Integer>> path = state.pathfinder2D.findPath(state.grid2D, agentSq, destinationSq)  ;
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
    public static List<Pair<Integer,Integer>> smoothenPath(List<Pair<Integer,Integer>> path) {
        if(path.size() <= 2) return path ;
        int k = 0 ;
        while(k < path.size() - 2) {
            var v1 = path.get(k) ;
            var v2 = path.get(k+1) ;
            var v3 = path.get(k+2) ;
            float gradient_v1v2 = gradient2D(v2.fst - v1.fst, v2.snd - v1.snd) ;
            float gradient_v2v3 = gradient2D(v3.fst - v2.fst, v3.snd - v2.snd) ;
            //if( (v2.fst - v1.fst == v3.fst - v2.fst)  && (v2.snd - v1.snd == v3.snd - v2.snd)) {
            if(gradient_v1v2 == gradient_v2v3) {
                // dy and dx between (v1,v2) and between (v2,v3) are the same
                // then they lie along the same line. We then drop v2:
                path.remove(k+1) ;
            }
            else {
                k++ ;
            }
        }
        return path ;
    }

    // for calculating the grasdient dz/dx ... rounded to 0.001
    static float gradient2D(int dx, int dz) {
        if(dx == 0) {
            if (dz>0) return Float.POSITIVE_INFINITY ;
            if (dz<0) return Float.NEGATIVE_INFINITY ;
            return 0 ;
        }
        float gradient = 1000f * ((float) dz) / ((float) dx) ;
        gradient = ((float) Math.round(gradient) )/1000f ;
        return gradient ;
    }

    public static GoalStructure veryclose2DTo(String goalname, Vec3 p) {
        if (goalname == null) {
            goalname = "very close to " + p ;
        }
        //float threshold = Grid2DNav.AGENT_WIDTH * 0.5f * Grid2DNav.AGENT_WIDTH * 0.5f ;
        float threshold = 1.7f ; // magic number :|
        return goal(goalname)
                .toSolve((Float square_distance) -> {
                    System.out.println(">> sq-dist " + square_distance) ;
                    return square_distance <= threshold ;
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

        Vec3 ZERO = new Vec3(0,0,0) ;

        return action("turning towards " + destination)
                .on((USeAgentState state) ->{
                    Vec3 dirToGo = Vec3.sub(destination,state.wom.position) ;
                    Vec3 forwardOrientation = state.orientationForward() ;
                    dirToGo.y = 0 ;
                    forwardOrientation.y = 0 ;
                    dirToGo = dirToGo.normalized() ;
                    forwardOrientation = forwardOrientation.normalized() ;
                    var cos_alpha = Vec3.dot(forwardOrientation,dirToGo) ;
                    System.out.println("** dir-to-go: " + dirToGo);
                    System.out.println("** hdir: " + forwardOrientation);
                    System.out.println("** cos_alpha: " + cos_alpha);


                    float turningSpeed = TURNING_SPEED ;
                    if(1 - cos_alpha < 0.005) { // the angle is quite aligned, the action is disabled
                        return null ;
                    }
                    else {
                        Vec3 normalVector = Vec3.cross(forwardOrientation,dirToGo) ;
                        if(cos_alpha >= THRESHOLD_ANGLE_TO_SLOW_TURNING) {
                            // the angle between the agent's own direction and target direction is less than 10-degree
                            // we reduce the turning-speed:
                            turningSpeed = TURNING_SPEED*0.25f ;
                        }
                        if (normalVector.y > 0) {
                            // the dir-to-go is to the "left"/counter-clockwise direction
                            turningSpeed = - turningSpeed ;
                        }
                        return new Pair<Float,Float>(cos_alpha, turningSpeed) ;
                    }
                })
                .do2((USeAgentState state) -> (Pair<Float,Float> queryResult) -> {
                    float cos_alpha = queryResult.fst ;
                    float turningSpeed = queryResult.snd ;
                    Vec2 turningVector = new Vec2(0,turningSpeed) ; // no turning ... we will just strafe
                    state.env().getController().getCharacter().moveAndRotate(SEBlockFunctions.toSEVec3(ZERO) ,turningVector, 0) ; // the last is "roll"
                    return cos_alpha ;
                }) ;
    }



}
