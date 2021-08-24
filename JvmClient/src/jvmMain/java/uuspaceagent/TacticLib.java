package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Vec2;


import java.util.List;

import static nl.uu.cs.aplib.AplibEDSL.* ;

public class TacticLib {

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

    public static float TURNING_SPEED = 20f ;

    /**
     * Walking speed. Set as 10f in the forward direction (relative to the agent's orientation).
     * The negative is intentional and correct :)
     */
    public static spaceEngineers.model.Vec3 WALK_SPEED = new spaceEngineers.model.Vec3(0,0,-10f) ;

    public static Tactic navigate2DTo(Vec3 destination) {

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
                    // direction vector to the next node:
                    Vec3 dirToGo = Vec3.sub(nextNodePos,state.wom.position).normalized() ;
                    Vec3 agentHdir = state.orientationForward() .normalized();
                    // for calculating 2D rotation we ignore the y-value:
                    dirToGo.y = 0 ;
                    agentHdir.y = 0 ;
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
                            forward_speed = forward_speed.times(0.33f) ;
                        }
                    }
                    else {
                        // else, for now we will turn without moving. :|
                        forward_speed = new spaceEngineers.model.Vec3(0,0,0)  ;
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

                    Vec2 turningVector = new Vec2(0, turningSpeed) ;

                    // Now send a command to move the agent:

                    //if(cos_alpha<=0) forward_speed = new spaceEngineers.model.Vec3(0,0,0) ;
                    state.env().getController().getCharacter().moveAndRotate(forward_speed , turningVector, 0) ; // the last is "roll"
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

}
